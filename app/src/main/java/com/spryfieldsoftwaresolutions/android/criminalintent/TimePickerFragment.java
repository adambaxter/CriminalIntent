package com.spryfieldsoftwaresolutions.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.lang.reflect.Array;

/**
 * Created by slim on 07/02/18.
 * <p>
 * A fragment that allows the user to pick the time of day that a crime took place.
 */

public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME =
            "com.spryfieldsoftwaresolutions.android.criminalintent.time";

    private static final String ARG_TIME = "time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(int hours, int mins) {
        Bundle args = new Bundle();
        int[] time = {hours, mins};
        args.putSerializable(ARG_TIME, time);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int[] time = (int[]) getArguments().getSerializable(ARG_TIME);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mTimePicker = v.findViewById(R.id.dialog_time_picker);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hours = mTimePicker.getCurrentHour();
                                int mins = mTimePicker.getCurrentMinute();
                                int[] time = {hours, mins};
                                Log.d("TIME []", "time[0]: " + time[0] + "\ntime[1]: " + time[1]);
                                sendResult(Activity.RESULT_OK, time);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, int[] time) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
