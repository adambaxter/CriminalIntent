package com.spryfieldsoftwaresolutions.android.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.spryfieldsoftwaresolutions.android.criminalintent.database.CrimeBaseHelper;

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

    private Map<UUID, Crime> mCrimes;
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
        mCrimes = new LinkedHashMap<>();
    }

    public void addCrime(Crime c) {
        mCrimes.put(c.getId(), c);
    }

    public List<Crime> getCrimes(){
        return new ArrayList<>(mCrimes.values());
    }

    public Crime getCrime(UUID id){
        return mCrimes.get(id);
    }

    public void removeCrime(UUID id) {
        mCrimes.remove(id);
    }

    public int numberOfCrimes() {
        return mCrimes.size();
    }
}
