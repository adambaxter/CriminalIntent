package com.spryfieldsoftwaresolutions.android.criminalintent.database;

/**
 * Created by Adam Baxter on 11/02/18.
 *
 * Database Schema
 */

public class CrimeDbSchema {
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String TIME = "time";
            public static final String SOLVED = "solved";
        }
    }
}
