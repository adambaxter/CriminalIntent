package com.spryfieldsoftwaresolutions.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.spryfieldsoftwaresolutions.android.criminalintent.database.CrimeBaseHelper;
import com.spryfieldsoftwaresolutions.android.criminalintent.database.CrimeDbSchema;
import com.spryfieldsoftwaresolutions.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by slim on 02/02/18.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    //private Map<UUID, Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();
        //  mCrimes = new LinkedHashMap<>();
    }

    public void addCrime(Crime c) {
        //mCrimes.put(c.getId(), c);
        ContentValues values = getContentValues(c);

        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public List<Crime> getCrimes(){
        //return new ArrayList<>(mCrimes.values());
        return new ArrayList<>();
    }

    public Crime getCrime(UUID id){
        //return mCrimes.get(id);
        return null;
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void removeCrime(UUID id) {
        //mCrimes.remove(id);
    }

    public int numberOfCrimes() {
        //return mCrimes.size();
        return 0;
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate());
        values.put(CrimeTable.Cols.TIME, crime.getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);

        return values;
    }
}
