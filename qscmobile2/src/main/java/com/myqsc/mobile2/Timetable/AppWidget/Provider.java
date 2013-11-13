package com.myqsc.mobile2.Timetable.AppWidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.myqsc.mobile2.R;

public class Provider extends AppWidgetProvider {

    public static final String ACTION_TIMETABLE_UPDATED = "com.myqsc.mobile2.Timetable.AppWidget.TimetableUpdated";

    private static final int DATE_NUMBER = 3;
    private static final int TASK_NUMBER = 5;


    private void updateView(int id) {

    }


    // TODO: Receive data update broadcast
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_TIMETABLE_UPDATED)) {
            // TODO: updateTimetable();
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews appWidgetViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable);

        // Remove sub views in case the host recycles the view, while this also removes the loading TextView.
        appWidgetViews.removeAllViews(R.id.appwidget_timetable_date_list);
        appWidgetViews.removeAllViews(R.id.appwidget_timetable_task_list);

        RemoteViews subViews;

        for (int i = 0; i != DATE_NUMBER; ++i) {
            subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_date);
            subViews.setTextViewText(R.id.appwidget_timetable_date, "周零"/* TODO: Use SimpleDateFormat */);
            appWidgetViews.addView(R.id.appwidget_timetable_date_list, subViews);
        }

        for (int i = 0; i != TASK_NUMBER; ++i) {
            subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task_list_divider);
            subViews.setImageViewResource(R.id.appwidget_timetable_task_list_divider, R.drawable.appwidget_timetable_task_list_divider_scheduled);
            appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

            subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task);
            subViews.setTextViewText(R.id.appwidget_timetable_task_time_start, "00:00"/* TODO */);
            subViews.setTextViewText(R.id.appwidget_timetable_task_time_end, "24:00"/* TODO */);
            subViews.setTextViewText(R.id.appwidget_timetable_task_name, "任务名称"/* TODO */);
            subViews.setTextViewText(R.id.appwidget_timetable_task_detail, "任务详细信息"/* TODO */);
            appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);
        }
        subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task_list_divider);
        subViews.setImageViewResource(R.id.appwidget_timetable_task_list_divider, R.drawable.appwidget_timetable_task_list_divider_scheduled);
        appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

        appWidgetManager.updateAppWidget(appWidgetIds, appWidgetViews);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

}
