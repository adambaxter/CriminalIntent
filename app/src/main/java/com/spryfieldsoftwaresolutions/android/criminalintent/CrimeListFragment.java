package com.spryfieldsoftwaresolutions.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.UUID;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;

/**
 * Created by Adam Baxter on 02/02/18.
 *
 * Fragment that handles showing all the crimes in a list view.
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private Button mEmptyCrimeListButton;
    private TextView mEmptyCrimeListTextView;
    private Callbacks mCallbacks;

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final String DIALOG_DELETE = "DialogDeleteCrime";

    //  public static final float ALPHA_FULL = 1.0f;
    private static final int REQUEST_TARGET = 0;

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onCrimeSelected(Crime crime);

        void onCrimeDeleted(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycle_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setItemTouchHelper();

        mEmptyCrimeListButton = view.findViewById(R.id.empty_crime_list_add_crime_button);
        mEmptyCrimeListTextView = view.findViewById(R.id.empty_crime_list_text);

        setVisibility();

        mEmptyCrimeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), crime.getId());
                startActivity(intent);

            }
        });

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        setVisibility();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.replaceList(crimes);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    private void setItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                Crime crime = mAdapter.mCrimes.get(position);

                if (direction == ItemTouchHelper.LEFT) {
                    mCallbacks.onCrimeDeleted(crime);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
/**                Resources res = getResources();

 if( actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
 // Get RecyclerView item from the VIewHolder
 View itemView = viewHolder.itemView;

 Paint p = new Paint();
 Bitmap icon;

 if(dX < 0){

 icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_menu_delete);

 // Set your color for negative displacement
 p.setColor(res.getColor(R.color.colorAccent));

 //Draw Rect with varying left side, equal to items right displacement
 //plus negative displacement dX
 c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
 (float) itemView.getRight(), (float) itemView.getBottom(), p);

 // Set the image icon for Left swipe
 c.drawBitmap(icon,
 (float) itemView.getRight() - icon.getWidth(),
 (float) itemView.getTop() + ((float) itemView.getBottom() -
 (float) itemView.getTop() - icon.getHeight()) / 2, p);

 }
 final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
 viewHolder.itemView.setAlpha(alpha);
 viewHolder.itemView.setTranslationX(dX);
 } else {
 super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
 }

 **/
                // get item
                View itemview = viewHolder.itemView;
                // get an icon from drawable folder
                Drawable deleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_delete);
                // get height and width sizes from layout
                float IcontHeight = deleteIcon.getIntrinsicHeight();
                float IconWidth = deleteIcon.getIntrinsicWidth();
                // get item's bottom and Top size
                float itemHeight = itemview.getBottom() - itemview.getTop();

                if (actionState == ACTION_STATE_SWIPE) {   // user is swipe

                    Resources r = getResources();   // as you read
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    // create paint object
                    // get layout all values from inflate crime_fragment_list.xml
                    RectF layout = new RectF(itemview.getRight() + dX, itemview.getTop(), itemview.getRight(), itemview.getBottom());
                    // set color
                    paint.setColor(r.getColor(R.color.colorAccent));
                    // drawing canvas
                    c.drawRect(layout, paint);

                    // to calculate deleteIcon object that necessary values
                    int deleteIconTop = (int) (itemview.getTop() + (itemHeight - IcontHeight) / 2);
                    int deleteIconBottom = (int) (deleteIconTop + IcontHeight);
                    int deleteIconMargin = (int) ((itemHeight - IcontHeight) / 2);
                    int deleteIconLeft = (int) (itemview.getRight() - deleteIconMargin - IconWidth);
                    int deleteIconRight = (int) itemview.getRight() - deleteIconMargin;
                    // then set boundry that get values above
                    deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                    // to add canvas
                    deleteIcon.draw(c);


                    getDefaultUIUtil().onDraw(c, recyclerView, viewHolder.itemView, dX, dY, actionState, isCurrentlyActive);

                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mCrimeRecyclerView);
    }

    public void deleteCrime(UUID crimeId) {
        CrimeLab.get(getActivity()).removeCrime(crimeId);
    }

    private abstract class AbstractCrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        private Crime mCrime;

        public AbstractCrimeHolder(LayoutInflater inflater, ViewGroup parent, int layoutId) {
            super(inflater.inflate(layoutId, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);

        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            String dateAndTimeString = getString(R.string.crime_list_date_time_string, mCrime.getDate(), mCrime.getTime());
            mDateTextView.setText(dateAndTimeString);
            if (!mCrime.isRequiresPolice()) {
                mSolvedImageView.setVisibility(mCrime.isSolved() ? View.VISIBLE : View.INVISIBLE);
            }

        }

        public Crime getCrime() {
            return mCrime;
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onCrimeSelected(mCrime);
        }

    }

    private class CrimeHolder extends AbstractCrimeHolder {
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater, parent, R.layout.list_item_crime);
        }

    }

    private class PoliceCrimeHolder extends AbstractCrimeHolder {
        public PoliceCrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater, parent, R.layout.list_item_police_crime);
        }
    }


    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int LIST_ITEM_CRIME = 0;
        private static final int LIST_ITEM_POLICE_CRIME = 1;

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public AbstractCrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if (viewType == LIST_ITEM_CRIME) {
                return new CrimeHolder(layoutInflater, parent);
            } else if (viewType == LIST_ITEM_POLICE_CRIME) {
                return new PoliceCrimeHolder(layoutInflater, parent);
            } else {
                return null;
            }

        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            if (holder instanceof CrimeHolder) {
                ((CrimeHolder) holder).bind(crime);
            } else if (holder instanceof PoliceCrimeHolder) {
                ((PoliceCrimeHolder) holder).bind(crime);
            }


        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position);
            if (crime.isRequiresPolice()) {
                return LIST_ITEM_POLICE_CRIME;
            }
            return LIST_ITEM_CRIME;
        }

        public void replaceList(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }

    public void setVisibility() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        if (crimeLab.numberOfCrimes() == 0) {
            mEmptyCrimeListButton.setVisibility(View.VISIBLE);
            mEmptyCrimeListTextView.setVisibility(View.VISIBLE);
        } else {
            mEmptyCrimeListButton.setVisibility(View.GONE);
            mEmptyCrimeListTextView.setVisibility(View.GONE);
        }
    }
}
