package com.johnvandenberg.homework.data.local;


import android.net.Uri;
import android.provider.BaseColumns;


public final class HomeworkContract {

    public static final String AUTHORITY = "com.johnvandenberg.provider.homework";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + HomeworkContract.HomeworkEntry.TABLE_NAME);

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private HomeworkContract() {}

    /* Inner class that defines the table contents */
    public static class HomeworkEntry implements BaseColumns {
        public static final String TABLE_NAME = "homework";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_FINISHED = "finished";
    }
}
