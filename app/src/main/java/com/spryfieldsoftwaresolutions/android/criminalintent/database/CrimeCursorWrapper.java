package com.spryfieldsoftwaresolutions.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.spryfieldsoftwaresolutions.android.criminalintent.Crime;
import com.spryfieldsoftwaresolutions.android.criminalintent.database.CrimeDbSchema.CrimeTable;

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

        return null;

    }
}
