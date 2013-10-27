package com.myqsc.mobile2.Timetable.AppWidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.myqsc.mobile2.R;

public class Provider extends AppWidgetProvider {

    // TODO: Receive data update broadcast
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews appWidgetViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable);
        appWidgetManager.updateAppWidget(appWidgetIds, appWidgetViews);
    }

    // TODO: Add alarm service?
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

}
