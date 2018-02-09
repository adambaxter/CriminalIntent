package com.spryfieldsoftwaresolutions.android.criminalintent;

import android.content.Context;

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

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes = new LinkedHashMap<>();
        for(int i=0; i<100;i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 3 == 0);
            crime.setTime();
            //crime.setRequiresPolice(i % 2 == 1);
            mCrimes.put(crime.getId(), crime);
        }
    }

    public List<Crime> getCrimes(){
        return new ArrayList<>(mCrimes.values());
    }

    public Crime getCrime(UUID id){
        return mCrimes.get(id);
    }

}
