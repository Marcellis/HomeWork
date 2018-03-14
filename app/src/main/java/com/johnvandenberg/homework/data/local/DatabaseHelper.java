package com.johnvandenberg.homework.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "homework.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "CREATE TABLE " + HomeworkContract.HomeworkEntry.TABLE_NAME + "(" +
                    HomeworkContract.HomeworkEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + HomeworkContract.HomeworkEntry.COLUMN_TITLE + " TEXT, "
                    + HomeworkContract.HomeworkEntry.COLUMN_SUBJECT + " TEXT, "
                    + HomeworkContract.HomeworkEntry.COLUMN_DATE + " TEXT, "
                    + HomeworkContract.HomeworkEntry.COLUMN_FINISHED + " INTEGER )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HomeworkContract.HomeworkEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
