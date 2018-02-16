package com.spryfieldsoftwaresolutions.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.widget.CompoundButton.*;

/**
 * Created by Adam Baxter on 02/02/18.
 * <p>
 * Fragment that allows a user to create a crime.
 */

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private ImageButton mPhotoButton;
    private TextView mTitleLabel;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mSuspectButton;
    private Button mCallSuspectButton;
    private String mPhoneNumber;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private int mPhotoviewHeight;
    private int mPhotoviewWidth;


    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final String DIALOG_DELETE = "DialogDeleteCrime";
    private static final String DIALOG_IMAGE = "DialogImage";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mPhotoView = v.findViewById(R.id.crime_photo);
        mPhotoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updatePhotoView();
                //mPhotoviewHeight = mPhotoView.getHeight();
                //mPhotoviewWidth = mPhotoView.getWidth();
                Log.e("RUNN DIMENSIONS", "mPhotoWidth: " + mPhotoView.getWidth() + "\nmPhotoView: " + mPhotoView.getHeight());
                mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
        /** mPhotoView.post(new Runnable() {
            @Override
            public void run() {
        updatePhotoView();
                Log.e("RUNN DIMENSIONS", "mPhotoWidth: " + mPhotoView.getWidth() + "\nmPhotoView: " + mPhotoView.getHeight());

            }
        });**/

        updatePhotoView();
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
//                Log.e("PHOTOVIEW", "mCrime.getPhotoFilename():" + mCrime.getPhotoFile());
                ImageFragment imageFragment = ImageFragment.newInstance(mPhotoFile);
                imageFragment.show(manager, DIALOG_IMAGE);
            }
        });

        mPhotoButton = v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.spryfieldsoftwaresolutions.android.criminalintent.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivites = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivites) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });


        mTitleLabel = v.findViewById(R.id.crime_title_label);

        mTitleField = v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        mDateButton = v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getUnformattedDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);

            }
        });

        mTimeButton = v.findViewById(R.id.crime_time);
        if (mCrime.getTime() == null) {
            mCrime.setTime();
        }
        mTimeButton.setText(mCrime.getTime());
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mCrime.getHours(), mCrime.getMins());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI)
                .setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

        mSuspectButton = v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

//        final Intent getPhoneNumber = new Intent(Intent.ACTION_PICK,
        //              ContactsContract.CommonDataKinds.Phone.CONTENT_URI);

        mCallSuspectButton = v.findViewById(R.id.call_suspect);
        if (mSuspectButton.getText() == getString(R.string.crime_suspect_text)) {
            mCallSuspectButton.setEnabled(false);
        }

        mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + mPhoneNumber);
                Intent dialNumber = new Intent(Intent.ACTION_DIAL);
                dialNumber.setData(number);
                dialNumber = Intent.createChooser(dialNumber, getString(R.string.dial_number));
                startActivity(dialNumber);
            }
        });


        mReportButton = v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setChooserTitle(getString(R.string.send_report))
                        .startChooser();
            }
        });

        mSolvedCheckBox = v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_TIME) {
            int[] time = (int[]) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setTimeFromInts(time[0], time[1]);
            mTimeButton.setText(mCrime.getTime());

        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            /**specify which fields you want your query to return
             * values for
             **/
            String[] queryFields = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                    // ContactsContract.Contacts.LOOKUP_KEY,

            };
            /**
             * Perform your query - the contactUri is like a "where"
             * clause here
             **/
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);

            try {
                // Check that you got results
                if (c.getCount() == 0) {
                    return;
                }

                /**
                 * Pull out the DISPLAY_NAME and NUMBER from the first row of data
                 **/
                c.moveToFirst();
                String suspect = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                mPhoneNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
                mCallSuspectButton.setEnabled(true);
            } finally {
                c.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.spryfieldsoftwaresolutions.android.criminalintent.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_crime:
                FragmentManager manager = getFragmentManager();
                DeleteCrimeFragment dialog = DeleteCrimeFragment
                        .newInstance(mCrime.getId());
                dialog.show(manager, DIALOG_DELETE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate());
    }

    private String getCrimeReport() {
        String solvedString = null;

        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat,
                mCrime.getUnformattedDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);

        return report;

    }

    private void updatePhotoView() {


        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
            mPhotoView.setClickable(false);
        } else {

            //Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),
            //  getActivity());
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),
                    mPhotoView.getWidth(), mPhotoView.getHeight());
            Log.e("IMG DIMENSIONS", "mPhotoWidth W : " + mPhotoView.getWidth() + "\nmPhotoView H : " + mPhotoView.getHeight());
            mPhotoView.setImageBitmap(bitmap);
            mPhotoView.setClickable(true);

        }
    }
}
