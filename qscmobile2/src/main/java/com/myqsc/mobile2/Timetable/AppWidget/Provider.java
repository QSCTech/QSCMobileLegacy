package com.myqsc.mobile2.Timetable.AppWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.Timetable.Information.Task;
import com.myqsc.mobile2.Timetable.Information.TimetableManager;
import com.myqsc.mobile2.Utility.TimeUtils;
import com.myqsc.mobile2.uti.LogHelper;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.SortedSet;

public class Provider extends AppWidgetProvider {

    public static final String ACTION_DATE_BACKWARD = "com.myqsc.mobile2.Timetable.AppWidget.DATE_BACKWARD";

    public static final String ACTION_DATE_FORWARD = "com.myqsc.mobile2.Timetable.AppWidget.DATE_FORWARD";

    private static final int DATE_NUMBER_MAXIMUM = 7;

    private static final int DATE_NUMBER_SHOWN = 3;

    private static final int TASK_NUMBER_SHOWN = 5;

    private static class CachedInfoHolder {
        Calendar date;
        SortedSet<Task> timetable;
    }

    // FIXME: Use SharedPreferences instead of static variables because process or JVM might get killed.
    private static CachedInfoHolder[] cachedInfo = new CachedInfoHolder[DATE_NUMBER_MAXIMUM];

    private static HashMap<Integer, Integer> appWidgetDateIndex = new LinkedHashMap<Integer, Integer>();


    void updateCachedInfo(Context context, boolean forceUpdate) {

        if (forceUpdate || cachedInfo[0] == null || !TimeUtils.isToday(cachedInfo[DATE_NUMBER_MAXIMUM / 2].date)) {

            // Cache the starting date.
            Calendar dateStart = TimeUtils.getToday();
            dateStart.add(Calendar.DAY_OF_YEAR, - DATE_NUMBER_MAXIMUM / 2);

            // Update CachedInfo
            for (int i = 0; i != DATE_NUMBER_MAXIMUM; ++i) {

                // Create the CachedInfoHolder for the first update.
                if (cachedInfo[i] == null) {
                    cachedInfo[i] = new CachedInfoHolder();
                }

                // Update the date.
                cachedInfo[i].date = (Calendar) dateStart.clone();
                cachedInfo[i].date.add(Calendar.DAY_OF_YEAR, i);

                // Update the timetable.
                // FIXME: Should be moved to main application
                if (!TimetableManager.isInitialized()) {
                    LogHelper.i("TimetableManager initialized in AppWidget.");
                    TimetableManager.initialize(context.getApplicationContext());
                }
                cachedInfo[i].timetable = TimetableManager.getInstance().getTimetable(cachedInfo[i].date);
            }
        }
    }

    private void buildUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews appWidgetViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable);

        // Remove sub views in case the host recycles the view, while this also removes the loading TextView.
        appWidgetViews.removeAllViews(R.id.appwidget_timetable_date_list);
        appWidgetViews.removeAllViews(R.id.appwidget_timetable_task_list);

        // Add subviews.
        RemoteViews subViews;

        // Cache the date indexes.
        int dateIndex = DATE_NUMBER_MAXIMUM / 2;
        if (appWidgetDateIndex.containsKey(appWidgetId)) {
            dateIndex = appWidgetDateIndex.get(appWidgetId);
        } else {
            appWidgetDateIndex.put(appWidgetId, dateIndex);
        }
        int dateShownIndexStart = dateIndex - DATE_NUMBER_SHOWN / 2;

        // Determine if navigable.
        boolean leftNavigable = true, rightNavigable = true;
        if (dateIndex == 0) {
            leftNavigable = false;
        } else if (dateIndex == DATE_NUMBER_MAXIMUM - 1) {
            rightNavigable = false;
        }

        // Create the intents for clicking.
        PendingIntent dateBackwardPendingIntent = null, dateForwardPendingIntent = null;
        if (leftNavigable) {
            Intent dateBackwardIntent = new Intent(context, this.getClass());
            dateBackwardIntent.setAction(ACTION_DATE_BACKWARD);
            dateBackwardIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            dateBackwardPendingIntent = PendingIntent.getBroadcast(context, 0, dateBackwardIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        if (rightNavigable) {
            Intent dateForwardIntent = new Intent(context, this.getClass());
            dateForwardIntent.setAction(ACTION_DATE_FORWARD);
            dateForwardIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            dateForwardPendingIntent = PendingIntent.getBroadcast(context, 0, dateForwardIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        // Set text color and PendingIntent for date navigation view.
        //
        // Canceling PendingIntent in case the view is reused causes exception, setBoolean with setClickable
        // causes exception, set Boolean with setEnabled does nothing; don't know how to disable it.
        // Dealing with this in onDateChanged.
        if (leftNavigable) {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_navigation_left, context.getResources().getColor(R.color.white));
            appWidgetViews.setOnClickPendingIntent(R.id.appwidget_timetable_date_navigation_left, dateBackwardPendingIntent);
        } else {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_navigation_left, context.getResources().getColor(R.color.white_fading));
            // dateBackwardPendingIntent.cancel();
        }
        if (rightNavigable) {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_navigation_right, context.getResources().getColor(R.color.white));
            appWidgetViews.setOnClickPendingIntent(R.id.appwidget_timetable_date_navigation_right, dateForwardPendingIntent);
        } else {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_navigation_right, context.getResources().getColor(R.color.white_fading));
            // dateForwardPendingIntent.cancel();
        }

        // Add dates to view.
        for (int i = 0; i != DATE_NUMBER_SHOWN; ++i) {

            subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_date);

            // Determine whether the date view should be empty or not.
            if (dateShownIndexStart + i >= 0 && dateShownIndexStart + i <= DATE_NUMBER_MAXIMUM - 1) {

                // Add content to the date view.
                if (dateShownIndexStart + i == DATE_NUMBER_MAXIMUM / 2) {
                    subViews.setTextViewText(R.id.appwidget_timetable_date, context.getString(R.string.appwidget_timetable_today));
                } else {
                    subViews.setTextViewText(R.id.appwidget_timetable_date, TimeUtils.getWeekdayString(cachedInfo[dateShownIndexStart + i].date));
                }
                if (i == DATE_NUMBER_SHOWN / 2) {
                    subViews.setTextColor(R.id.appwidget_timetable_date, context.getResources().getColor(R.color.white));
                } else {
                    subViews.setTextColor(R.id.appwidget_timetable_date, context.getResources().getColor(R.color.white_fading));
                    if (i < DATE_NUMBER_SHOWN / 2 && leftNavigable) {
                        subViews.setOnClickPendingIntent(R.id.appwidget_timetable_date, dateBackwardPendingIntent);
                    } else if (rightNavigable) {
                        subViews.setOnClickPendingIntent(R.id.appwidget_timetable_date, dateForwardPendingIntent);
                    }
                }
            }

            appWidgetViews.addView(R.id.appwidget_timetable_date_list, subViews);
        }

        // Add tasks to view.
        // TODO: Change the tasks displayed according to current time.
        SortedSet<Task> timetable = cachedInfo[dateIndex].timetable;

        if (timetable.size() == 0) {

            // Add no-task view
            subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_notask);
            appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

        } else {

            // Add the first divider
            // TODO: Check if this should be marked active.
            subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task_list_divider);
            subViews.setImageViewResource(R.id.appwidget_timetable_task_list_divider, R.drawable.appwidget_timetable_task_list_divider_normal);
            appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

            Iterator<Task> iter = timetable.iterator();

            for (int i = 0; i != TASK_NUMBER_SHOWN; ++i) {

                if (iter.hasNext()) {

                    Task task = iter.next();

                    // Add task view
                    // TODO: Set text color
                    subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task);
                    subViews.setTextViewText(R.id.appwidget_timetable_task_name, task.getName());
                    subViews.setTextViewText(R.id.appwidget_timetable_task_detail, task.getDetail());
                    subViews.setTextViewText(R.id.appwidget_timetable_task_time_start, TimeUtils.getHourMinuteString(task.getStartTime()));
                    subViews.setTextViewText(R.id.appwidget_timetable_task_time_end, TimeUtils.getHourMinuteString(task.getEndTime()));
                    appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

                    // Add divider
                    subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task_list_divider);
                    subViews.setImageViewResource(R.id.appwidget_timetable_task_list_divider, R.drawable.appwidget_timetable_task_list_divider_normal);
                    appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

                } else {

                    // Add empty views for spacing in LinearLayout

                    // Add default (empty) task view
                    subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task);
                    appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

                    // Add transparent divider
                    subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task_list_divider);
                    subViews.setImageViewResource(R.id.appwidget_timetable_task_list_divider, R.drawable.appwidget_timetable_task_list_divider_transparent);
                    appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);
                }
            }
        }

        // Update AppWidget
        appWidgetManager.updateAppWidget(appWidgetId, appWidgetViews);
        LogHelper.i("appWidgetId: " + appWidgetId);
        LogHelper.i("dateIndex:" + dateIndex);
    }

    private void onDateChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int dateDelta) {

        LogHelper.i("appWidgetId: " + appWidgetId);
        int dateIndex = appWidgetDateIndex.get(appWidgetId);

        // Filter invalid date changes.
        if ((dateIndex == 0 && dateDelta < 0) || (dateIndex == DATE_NUMBER_MAXIMUM - 1 && dateDelta > 0)) {
            LogHelper.i("Ignoring intent for appWidgetId: " + appWidgetId);
            return;
        }

        appWidgetDateIndex.put(appWidgetId, dateIndex + dateDelta);

        buildUpdate(context, appWidgetManager, appWidgetId);
    }

    // TODO: Receive data update broadcast
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (ACTION_DATE_BACKWARD.equals(action)) {
            onDateChanged(context, AppWidgetManager.getInstance(context), intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID), -1);
        } else if (ACTION_DATE_FORWARD.equals(action)) {
            onDateChanged(context, AppWidgetManager.getInstance(context), intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID), 1);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    // TODO: Remove dummy settings from layouts.
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Check if the cached info should be updated
        updateCachedInfo(context, false);

        for (int appWidgetId : appWidgetIds) {
            LogHelper.i("appWidgetId: " + appWidgetId);
            buildUpdate(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

        // Remove date index for deleted AppWidgets
        for (int appWidgetId : appWidgetIds) {
            appWidgetDateIndex.remove(appWidgetId);
        }

        super.onDeleted(context, appWidgetIds);
    }
}
