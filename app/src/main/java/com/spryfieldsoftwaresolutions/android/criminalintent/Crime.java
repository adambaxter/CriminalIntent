package com.spryfieldsoftwaresolutions.android.criminalintent;


import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Adam Baxter on 02/02/18.
 * <p>
 * Crime Object that holds all information about a crime.
 */

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mTime;
    private int mHours;
    private int mMins;

    private boolean mSolved;
    private String mSuspect;
    private boolean mRequiresPolice;


    public Crime() {
        this(UUID.randomUUID());

    }

    public Crime(UUID id) {
        mId = id;
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
        return FormatDateAndTime.formatDate(mDate);
    }

    public Date getUnformattedDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setTime() {
        Date date = new Date();
        setTime(date);
    }

    public void setTime(Date date) {

        mTime = FormatDateAndTime.formatTime(date);

        setHours(FormatDateAndTime.formatTimeHour(date));
        setMins(FormatDateAndTime.formatTimeMins(date));

    }

    public void setTime(String time) {
        mTime = time;
    }

    public void setTimeFromInts(int hour, int min) {
        mTime = FormatDateAndTime.convertTime(hour, min);
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

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

}

