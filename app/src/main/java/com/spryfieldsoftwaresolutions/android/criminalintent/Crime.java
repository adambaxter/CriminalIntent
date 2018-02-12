package com.spryfieldsoftwaresolutions.android.criminalintent;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by slim on 02/02/18.
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

    /**   private String formatDate(Date date) {
     SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d, yyyy", Locale.CANADA);

     return sdf.format(date);

     }

     private String formatTime(Date date) {
     SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.CANADA);
     SimpleDateFormat hour = new SimpleDateFormat("h", Locale.CANADA);
     SimpleDateFormat min = new SimpleDateFormat("mm", Locale.CANADA);

     setHours(Integer.parseInt(hour.format(date)));
     setMins(Integer.parseInt(min.format(date)));

     return sdf.format(date);
     }

     public String convertTime(int hour, int min) {
     String hours = "";
     String mins = "";
     Boolean AM = false;

     if (hour == 0) {
     AM = true;
     hours = "12";

     } else if (hour >= 1 && hour <= 11) {
     hours = String.valueOf(hour);
     AM = true;

     } else if (hour == 12) {
     hours = String.valueOf(hour);
     AM = false;

     } else if (hour > 12) {
     hours = String.valueOf(hour - 12);
     }

     if (min < 10) {
     mins = "0" + min;
     } else {
     mins = String.valueOf(min);
     }

     if (AM) {
     return hours + ":" + mins + " AM";
     } else {
     return hours + ":" + mins + " PM";
     }
     }**/
}

