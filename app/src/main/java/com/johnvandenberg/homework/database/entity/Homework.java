package com.johnvandenberg.homework.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.database.Cursor;

@Entity(tableName = Homework.TABLE_NAME )
public class Homework {

    public static final String TABLE_NAME = "homework";
    public static final String COLUMN_ID = "uid";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_FINISHED = "finished";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private long uid;

    @ColumnInfo(name = COLUMN_TITLE)
    public String title;

    @ColumnInfo(name = COLUMN_SUBJECT)
    private String subject;

    @ColumnInfo(name = COLUMN_DATE)
    public String date;

    @ColumnInfo(name = COLUMN_FINISHED)
    private int finished;

    // Constructors

    public Homework() {}

    public Homework(Cursor cursor) {
        uid         = cursor.getLong(cursor.getColumnIndex( COLUMN_ID ));
        title       = cursor.getString(cursor.getColumnIndex( COLUMN_TITLE ));
        subject     = cursor.getString(cursor.getColumnIndex( COLUMN_SUBJECT ));
        date        = cursor.getString(cursor.getColumnIndex( COLUMN_DATE ));
        finished    = cursor.getInt(cursor.getColumnIndex( COLUMN_FINISHED ));
    }

    public Homework(String title, String subject, String date, boolean finished) {
        this.title = title;
        this.subject = subject;
        this.date = date;
        this.finished = finished ? 1 : 0;
    }

    // Getters and setters
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    /**
     *
     * @param finished
     */
    public void setFinished(boolean finished) {
        this.finished = finished ? 1 : 0;
    }

    /**
     * Function for validating if the homework is finished
     * @return boolean
     */
    public boolean isFinished() {
        return finished == 1;
    }

    public static Homework fromContentValues(ContentValues values) {
        final Homework homework = new Homework();
        if (values.containsKey(COLUMN_ID)) {
            homework.uid = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_TITLE)) {
            homework.title = values.getAsString(COLUMN_TITLE);
        }
        if (values.containsKey(COLUMN_SUBJECT)) {
            homework.title = values.getAsString(COLUMN_SUBJECT);
        }
        if (values.containsKey(COLUMN_DATE)) {
            homework.title = values.getAsString(COLUMN_DATE);
        }
        if (values.containsKey(COLUMN_FINISHED)) {
            homework.title = values.getAsString(COLUMN_FINISHED);
        }

        return homework;
    }
}
