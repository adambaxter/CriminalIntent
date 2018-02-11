package com.spryfieldsoftwaresolutions.android.criminalintent;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.UUID;

/**
 * Created by slim on 10/02/18.
 */

public class DeleteCrimeFragment extends DialogFragment {

    public static final String EXTRA_CRIME =
            "com.sprfieldsoftwaresolutions.android.criminalintent.crime";

    private static final String ARG_CRIME_ID = "crimeId";

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
                        lab.removeCrime(uuid);
                        getActivity().onBackPressed();
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

}
