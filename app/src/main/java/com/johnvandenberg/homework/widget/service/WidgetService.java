package com.johnvandenberg.homework.widget.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.johnvandenberg.homework.widget.provider.ListProvider;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this.getApplicationContext()));
    }
}
