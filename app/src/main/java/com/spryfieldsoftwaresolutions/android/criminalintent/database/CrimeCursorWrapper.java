package com.spryfieldsoftwaresolutions.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.spryfieldsoftwaresolutions.android.criminalintent.Crime;
import com.spryfieldsoftwaresolutions.android.criminalintent.FormatDateAndTime;
import com.spryfieldsoftwaresolutions.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by slim on 12/02/18.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        String date = getString(getColumnIndex(CrimeTable.Cols.DATE));
        String time = getString(getColumnIndex(CrimeTable.Cols.TIME));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));

        Log.d("DB set date ------> ", "" + date);

        Date rawDate = new Date();
        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);  /** figure this out**/
        crime.setDate(date);
        crime.setTime(time);
        crime.setSolved(isSolved != 0);

        return crime;

    }
}
