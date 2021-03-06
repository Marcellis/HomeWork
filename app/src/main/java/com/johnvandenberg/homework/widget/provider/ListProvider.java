package com.johnvandenberg.homework.widget.provider;

import android.content.Context;
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

    public ListProvider(Context context) {
        Log.d( TAG, "ListProvider" );
        this.context = context;
    }

    @Override
    public void onCreate() {
        Log.d( TAG, "onCreate" );
    }

    @Override
    public void onDataSetChanged() {
        Log.d( TAG, "onDataSetChanged" );

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

        Cursor cursor = context.getContentResolver().query( uri, projection, null, null, Homework.COLUMN_ID + " ASC" );

        if( cursor != null ) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                Log.d( TAG, "addCursorToList: "+ i );

                homeworkList.add(new Homework(cursor));
            }

            cursor.close();
        }
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

        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

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
}
