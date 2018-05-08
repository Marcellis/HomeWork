package com.johnvandenberg.homework.widget.provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.johnvandenberg.homework.R;
import com.johnvandenberg.homework.widget.service.WidgetService;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds ) {

        for( int widgetId : appWidgetIds ) {
            RemoteViews remoteViews = updateWidgetListView( context, widgetId );

            appWidgetManager.notifyAppWidgetViewDataChanged( widgetId, R.id.list_view );
            appWidgetManager.updateAppWidget( widgetId, remoteViews );
        }
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        // Which layout to show on widget
        RemoteViews remoteViews = new RemoteViews( context.getPackageName(), R.layout.widget_layout );

        // RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);

        // Passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        //Setting a unique Uri to the intent
        svcIntent.setData(Uri.parse( svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        // Setting adapter to listview of the widget
        remoteViews.setRemoteAdapter( R.id.list_view, svcIntent);

        // Setting an empty view in case of no data
        remoteViews.setEmptyView( R.id.list_view, R.id.empty_view );

        return remoteViews;
    }
}
