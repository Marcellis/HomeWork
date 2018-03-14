package com.johnvandenberg.homework.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.johnvandenberg.homework.data.local.HomeworkContract;
import com.johnvandenberg.homework.data.model.Homework;
import com.johnvandenberg.homework.data.provider.HomeworkProvider;

public class HomeworkRepository {

    private final ContentResolver resolver;

    public HomeworkRepository(ContentResolver resolver) {
        this.resolver = resolver;
    }

    public void save(Homework homework) {
        ContentValues values = new ContentValues();
        values.put(HomeworkContract.HomeworkEntry.COLUMN_TITLE, homework.getTitle());
        values.put(HomeworkContract.HomeworkEntry.COLUMN_DATE, homework.getDate());
        values.put(HomeworkContract.HomeworkEntry.COLUMN_SUBJECT, homework.getSubject());
        values.put(HomeworkContract.HomeworkEntry.COLUMN_FINISHED, homework.getFinished());

        resolver.insert(HomeworkContract.CONTENT_URI, values);
    }

    public void update(Homework homework) {
        ContentValues values = new ContentValues();
        values.put(HomeworkContract.HomeworkEntry._ID, homework.getId());
        values.put(HomeworkContract.HomeworkEntry.COLUMN_TITLE, homework.getTitle());
        values.put(HomeworkContract.HomeworkEntry.COLUMN_DATE, homework.getDate());
        values.put(HomeworkContract.HomeworkEntry.COLUMN_SUBJECT, homework.getSubject());
        values.put(HomeworkContract.HomeworkEntry.COLUMN_FINISHED, homework.getFinished());

        Uri singleUri = ContentUris.withAppendedId(HomeworkContract.CONTENT_URI, homework.getId());
        resolver.update(singleUri, values, null, null);
    }

    public void delete(int id) {
        Uri singleUri = ContentUris.withAppendedId(HomeworkContract.CONTENT_URI, id);
        resolver.delete(singleUri, null, null);
    }
}
