package com.spryfieldsoftwaresolutions.android.criminalintent;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.UUID;

/**
 * Created by Adam Baxter on 10/02/18.
 *
 * Fragment that handles deleting crimes from the database.
 */


public class DeleteCrimeFragment extends DialogFragment {

    public static final String EXTRA_CRIME =
            "com.sprfieldsoftwaresolutions.android.criminalintent.crime";

    private Callbacks mCallbacks;

    private static final String ARG_CRIME_ID = "crimeId";

    /**
     * Required interface for hosting activities
     **/
    public interface Callbacks {
        void onCrimeDeleted(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;

    }

    public static DeleteCrimeFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, id);

        DeleteCrimeFragment fragment = new DeleteCrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final UUID uuid = (UUID) getArguments().getSerializable(ARG_CRIME_ID);

        return new AlertDialog.Builder(getActivity()).setTitle(R.string.delete_crime_title)
                .setMessage(R.string.delete_crime_messsage)
                .setPositiveButton(R.string.delete_crime_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CrimeLab lab = CrimeLab.get(getActivity());

                        if (getActivity().findViewById(R.id.detail_fragment_container) == null) {
                            lab.removeCrime(uuid);
                            getActivity().onBackPressed();

                        } else {
                            mCallbacks.onCrimeDeleted(lab.getCrime(uuid));
                        }

                    }
                })
                .setNegativeButton(R.string.delete_crime_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteCrimeFragment.this.getDialog().cancel();
                    }
                })
                .create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;

    }

}
