package com.johnvandenberg.homework.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.johnvandenberg.homework.data.local.DatabaseHelper;
import com.johnvandenberg.homework.data.local.HomeworkContract;


public class HomeworkProvider extends ContentProvider {

    // Uri code used to query all homework entities
    public static final int HOMEWORK = 100;
    // Uri code used to query a single homework entity by it's id
    public static final int HOMEWORK_ID = 101;

    private static final UriMatcher URI_MATCHER = buildUriMatcher();


    private DatabaseHelper dbHelper;

    public HomeworkProvider() {
    }

    public HomeworkProvider(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

       /*
         All paths added to the UriMatcher have a corresponding int.
         For each kind of uri you may want to access, add the corresponding match with addURI.
         The two calls below add matches for the task directory and a single item by ID.
        */
        uriMatcher.addURI(HomeworkContract.AUTHORITY, HomeworkContract.HomeworkEntry.TABLE_NAME, HOMEWORK);
        uriMatcher.addURI(HomeworkContract.AUTHORITY, HomeworkContract.HomeworkEntry.TABLE_NAME + "/#", HOMEWORK_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get access to underlying database (read-only for query)
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = URI_MATCHER.match(uri);
        Cursor cursor;

        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case HOMEWORK:
                cursor = db.query(
                        HomeworkContract.HomeworkEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case HOMEWORK_ID:
                // URI: content://<authority>/homework/#
                String id = uri.getPathSegments().get(1);

                // Selection is the _ID column = ?, and the Selection args = the row ID from the URI
                String mSelection = HomeworkContract.HomeworkEntry._ID + "=?";
                String[] mSelectionArgs = new String[]{id};

                // Construct a query as you would normally, passing in the selection/args
                cursor = db.query(
                        HomeworkContract.HomeworkEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (getContext() != null && getContext().getContentResolver() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = URI_MATCHER.match(uri);
        Uri mUri; // URI to be returned

        switch (match) {
            case HOMEWORK:
                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insert(HomeworkContract.HomeworkEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    mUri = ContentUris.withAppendedId(HomeworkContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to save row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (getContext() != null && getContext().getContentResolver() != null) {
            // Notify the resolver if the uri has been changed, and return the newly inserted URI
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return constructed uri (this points to the newly inserted row of data)
        return mUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = URI_MATCHER.match(uri);
        // Keep track of the number of deleted tasks
        int deletedEntries; // starts as 0

        // Delete a single row of data
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case HOMEWORK_ID:
                // Get the task ID from the URI path
                // URI: content://<authority>/reminders/#
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                deletedEntries = db.delete(
                        HomeworkContract.HomeworkEntry.TABLE_NAME,
                        HomeworkContract.HomeworkEntry._ID + "=?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (getContext() != null && getContext().getContentResolver() != null && deletedEntries != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedEntries;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updatedEntries;

        // match code
        int match = URI_MATCHER.match(uri);

        switch (match) {
            case HOMEWORK_ID:
                //update a single task by getting the id
                // URI: content://<authority>/reminders/#
                String id = uri.getPathSegments().get(1);
                //using selections
                updatedEntries = dbHelper.getWritableDatabase().update(
                        HomeworkContract.HomeworkEntry.TABLE_NAME, values,
                        HomeworkContract.HomeworkEntry._ID + "=?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (getContext() != null && getContext().getContentResolver() != null && updatedEntries != 0) {
            //set notifications if a task was updated
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedEntries;
    }
}
