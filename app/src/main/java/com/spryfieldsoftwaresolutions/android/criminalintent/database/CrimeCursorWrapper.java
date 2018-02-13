package com.spryfieldsoftwaresolutions.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.spryfieldsoftwaresolutions.android.criminalintent.Crime;
import com.spryfieldsoftwaresolutions.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Adam Baxter on 12/02/18.
 *
 * A wrapper to retrieve crime data from the database, and turn it into a crime object.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        String time = getString(getColumnIndex(CrimeTable.Cols.TIME));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setTime(time);
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);

        return crime;

    }
}
