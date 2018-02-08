package com.spryfieldsoftwaresolutions.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by slim on 02/02/18.
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    //private ArrayList<Integer> mLastPositionUpdated = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycle_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            //if (mLastPositionUpdated > -1) {
            //    mAdapter.notifyItemChanged(mLastPositionUpdated);
            //    Log.d("CRIME_LIST_FRAGMENT", "Updated position: " + mLastPositionUpdated);
            //    mLastPositionUpdated = -1;
            // } else {
                mAdapter.notifyDataSetChanged();
            //}
        }
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
            mDateTextView.setText(mCrime.getDate() + " at " + mCrime.getTime());
            if (!mCrime.isRequiresPolice()) {
                mSolvedImageView.setVisibility(mCrime.isSolved() ? View.VISIBLE : View.INVISIBLE);
            }

        }

        public Crime getCrime() {
            return mCrime;
        }

        @Override
        public void onClick(View view) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            /** Log.d("ADAPTOR POSITION", "#" + this.getAdapterPosition());
             mLastPositionUpdated.add(this.getAdapterPosition());
             for(int i=0;i<mLastPositionUpdated.size()-1;i++){
             Log.d("LAST_POSITION_UPDATED", mLastPositionUpdated.get(i).toString() );
             }**/
            startActivity(intent);
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


    }

}
