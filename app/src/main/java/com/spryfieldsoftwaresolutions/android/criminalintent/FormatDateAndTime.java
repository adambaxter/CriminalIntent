package com.spryfieldsoftwaresolutions.android.criminalintent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by slim on 12/02/18.
 * <p>
 * Class to handle formatting dates and times.
 */

public class FormatDateAndTime {

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d, yyyy", Locale.CANADA);

        return sdf.format(date);

    }

    public static String dateToMilli(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("S", Locale.CANADA);

        return sdf.format(date);
    }

    public static String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.CANADA);

        return sdf.format(date);
    }

    public static int formatTimeHour(Date date) {
        SimpleDateFormat hour = new SimpleDateFormat("h", Locale.CANADA);

        return Integer.parseInt(hour.format(date));
    }


    public static int formatTimeMins(Date date) {
        SimpleDateFormat min = new SimpleDateFormat("h", Locale.CANADA);

        return Integer.parseInt(min.format(date));
    }

    public static String convertTime(int hour, int min) {
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
    }

}
