package com.spryfieldsoftwaresolutions.android.criminalintent;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by slim on 02/02/18.
 */

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mTime;
    private int mHours;
    private int mMins;
    private boolean mSolved;
    private boolean mRequiresPolice;


    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return formatDate(mDate);
    }

    public Date getUnformattedDate() {
        return mDate;
    }
    public void setDate(Date date) {
        mDate = date;
    }

    public void setTime() {
        mTime = formatTime(mDate);
    }

    public String getTime() {
        return mTime;
    }

    public void setHours(int hours) {
        mHours = hours;
    }

    public int getHours() {
        return mHours;
    }

    public void setMins(int mins) {
        mMins = mins;
    }

    public int getMins() {
        return mMins;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isRequiresPolice() {
        return mRequiresPolice;
    }

    public void setRequiresPolice(boolean requiresPolice) {
        mRequiresPolice = requiresPolice;
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d, yyyy");

        return sdf.format(date);

    }

    private String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");

        return sdf.format(date);
    }
}

