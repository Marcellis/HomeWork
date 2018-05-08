package com.johnvandenberg.homework.widget.provider;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.johnvandenberg.homework.R;
import com.johnvandenberg.homework.database.entity.Homework;
import com.johnvandenberg.homework.database.provider.HomeworkProvider;

import java.util.ArrayList;

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private static String TAG = "ListProvider";
    private ArrayList<Homework> homeworkList = new ArrayList<>();
    private Context context;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        Log.d( TAG, "ListProvider" );
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Log.d( TAG, "onCreate" );
        loadData();
    }

    @Override
    public void onDataSetChanged() {
        Log.d( TAG, "onDataSetChanged" );
        loadData();
    }

    @Override
    public void onDestroy() {
        Log.d( TAG, "onDestroy" );
        homeworkList.clear();
    }

    @Override
    public int getCount() {
        Log.d( TAG, "getCount" );
        return homeworkList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d( TAG, "getViewAt" );
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

        if( homeworkList.size() > 0 ) {
            remoteView.setTextViewText(R.id.title, homeworkList.get(position).getTitle());
            remoteView.setTextViewText(R.id.subject, homeworkList.get(position).getSubject());
            remoteView.setTextViewText(R.id.date, homeworkList.get(position).getDate());

            remoteView.setViewVisibility(R.id.finished, homeworkList.get(position).isFinished() ? View.VISIBLE : View.INVISIBLE);
        }

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        Log.d( TAG, "getLoadingView" );
        return null;
    }

    @Override
    public int getViewTypeCount() {
        Log.d( TAG, "getViewTypeCount" );
        return 1;
    }

    @Override
    public long getItemId(int i) {
        Log.d( TAG, "getItemId" );
        return i;
    }

    @Override
    public boolean hasStableIds() {
        Log.d( TAG, "hasStableIds" );
        return true;
    }

    private void loadData() {
        Log.d( TAG, "loadData" );

        // Reset the list!
        homeworkList.clear();

        Uri uri = HomeworkProvider.URI_HOMEWORK;
        String[] projection = {
                Homework.COLUMN_ID,
                Homework.COLUMN_TITLE,
                Homework.COLUMN_DATE,
                Homework.COLUMN_SUBJECT,
                Homework.COLUMN_FINISHED,
        };

        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = Homework.COLUMN_ID + " ASC";

        Cursor cursor = context.getContentResolver().query( uri, projection, selection, selectionArgs, sortOrder );
        addCursorToList( cursor );
    }

    private void addCursorToList( Cursor cursor ) {
        Log.d( TAG, "addCursorToList" );
        if( cursor != null ) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                homeworkList.add(new Homework(cursor));
            }

            cursor.close();
        }
    }
}
