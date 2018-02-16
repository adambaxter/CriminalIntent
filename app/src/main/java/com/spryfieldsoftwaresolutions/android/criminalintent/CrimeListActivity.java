package com.spryfieldsoftwaresolutions.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Adam Baxter on 02/02/18.
 *
 * Creates an activity to host a Crime List Fragment
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }
}
