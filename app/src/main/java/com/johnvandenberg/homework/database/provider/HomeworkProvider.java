package com.johnvandenberg.homework.database.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.johnvandenberg.homework.database.AppDatabase;
import com.johnvandenberg.homework.database.dao.HomeworkDao;
import com.johnvandenberg.homework.database.entity.Homework;


public class HomeworkProvider extends ContentProvider {

    /** The authority of this content provider. */
    public static final String AUTHORITY = "com.example.android.contentprovidersample.provider";

    /** The URI for the Cheese table. */
    public static final Uri URI_HOMEWORK = Uri.parse( "content://" + AUTHORITY + "/" + Homework.TABLE_NAME );

    // Uri code used to query all homework entities
    public static final int HOMEWORK = 100;

    // Uri code used to query a single homework entity by it's id
    public static final int HOMEWORK_ID = 101;

    /** The URI matcher. */
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, Homework.TABLE_NAME, HOMEWORK );
        URI_MATCHER.addURI(AUTHORITY, Homework.TABLE_NAME + "/*", HOMEWORK_ID );
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final int code = URI_MATCHER.match(uri);

        switch( code ) {
            case HOMEWORK:
            case HOMEWORK_ID:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }

                HomeworkDao homeworkDao = AppDatabase.getInstance( context ).homeworkDao();
                final Cursor cursor;

                if ( code == HOMEWORK ) {
                    cursor = homeworkDao.selectAll();
                } else {
                    cursor = homeworkDao.selectByUid( ContentUris.parseId(uri) );
                }
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch( URI_MATCHER.match(uri) ) {
            case HOMEWORK:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + Homework.TABLE_NAME;

            case HOMEWORK_ID:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Homework.TABLE_NAME;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final int code = URI_MATCHER.match(uri);

        switch( code ) {
            case HOMEWORK:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }

                final long id = AppDatabase.getInstance(context).homeworkDao().insert(Homework.fromContentValues( values ) );
                context.getContentResolver().notifyChange(uri, null);

                return ContentUris.withAppendedId(uri, id);

            case HOMEWORK_ID:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int code = URI_MATCHER.match(uri);

        switch( code ) {
            case HOMEWORK:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);

            case HOMEWORK_ID:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = AppDatabase.getInstance(context).homeworkDao().deleteByUid(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int code = URI_MATCHER.match(uri);

        switch( code ) {
            case HOMEWORK:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);

            case HOMEWORK_ID:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final Homework homework = Homework.fromContentValues(values);

                homework.setUid( ContentUris.parseId(uri) );

                final int count = AppDatabase.getInstance(context).homeworkDao().update(homework);
                context.getContentResolver().notifyChange(uri, null);
                return count;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
