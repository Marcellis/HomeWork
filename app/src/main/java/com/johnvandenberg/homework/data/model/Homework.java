package com.johnvandenberg.homework.data.model;

import android.database.Cursor;

import com.johnvandenberg.homework.data.local.HomeworkContract;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Homework implements Serializable {

    private int id;
    private String title;
    private String date;
    private String subject;
    private int finished;

    public Homework() {
    }

    public Homework(String title, String subject, String date, boolean finished) {
        this.title = title;
        this.subject = subject;
        this.date = date;
        this.finished = finished ? 1 : 0;
    }

    public Homework(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(HomeworkContract.HomeworkEntry._ID));
        title = cursor.getString(cursor.getColumnIndex(HomeworkContract.HomeworkEntry.COLUMN_TITLE));
        subject = cursor.getString(cursor.getColumnIndex(HomeworkContract.HomeworkEntry.COLUMN_SUBJECT));
        date = cursor.getString(cursor.getColumnIndex(HomeworkContract.HomeworkEntry.COLUMN_DATE));
        finished = cursor.getInt(cursor.getColumnIndex(HomeworkContract.HomeworkEntry.COLUMN_FINISHED));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isFinished() {
        return finished == 1;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished ? 1 : 0;
    }
}
