package com.myqsc.mobile2.Timetable.AppWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.Timetable.Information.Task;
import com.myqsc.mobile2.Timetable.Information.TimetableManager;
import com.myqsc.mobile2.Utility.TimeUtils;
import com.myqsc.mobile2.uti.LogHelper;

import java.util.Calendar;
import java.util.Iterator;
import java.util.SortedSet;

public class Provider extends AppWidgetProvider {

    // Actions

    private static final String ACTION_DATE_CHANGED = "com.myqsc.mobile2.Timetable.AppWidget.DATE_CHANGED";

    // Intent extras

    private static final String EXTRA_DATE_DELTA = "DATE_DELTA";

    // Constants for displaying information

    private static final int DATE_NUMBER_MAXIMUM = 7;

    private static final int DATE_NUMBER_SHOWN = 3;

    private static final int TASK_NUMBER_SHOWN = 5;

    // Store date index for each AppWidget in SharedPreferences and access via DateIndexManager.

    private static class DateIndexManager {

        private static String PREFERENCES_FILE_NAME = "com.myqsc.mobile2.Timetable.AppWidget";

        SharedPreferences sharedPreferences;


        DateIndexManager(Context context) {
            this.sharedPreferences = getSharedPreferences(context);
        }

        private static SharedPreferences getSharedPreferences(Context context) {
            return context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        }

        private static String getKey(int appWidgetId) {
            return appWidgetId + ".DateIndex";
        }

        // Return and set to default value (DATE_NUMBER_MAXIMUM / 2 as today) if the date index has not been set.
        private static int get(SharedPreferences sharedPreferences, int appWidgetId) {
            String key = getKey(appWidgetId);
            int dateIndex;
            if (sharedPreferences.contains(key)) {
                dateIndex = sharedPreferences.getInt(key, -1);
            } else {
                dateIndex = DATE_NUMBER_MAXIMUM / 2;
                // apply() requires API Level 9.
                sharedPreferences.edit().putInt(key, dateIndex).commit();
            }
            return dateIndex;
        }

        public static int get(Context context, int appWidgetId) {
            return get(getSharedPreferences(context), appWidgetId);
        }

        public int get(int appWidgetId) {
            return get(sharedPreferences, appWidgetId);
        }

        public static void set(SharedPreferences sharedPreferences, int appWidgetId, int dateIndex) {
            sharedPreferences.edit().putInt(getKey(appWidgetId), dateIndex).commit();
        }

        public static void set(Context context, int appWidgetId, int dateIndex) {
            set(getSharedPreferences(context), appWidgetId, dateIndex);
        }

        public void set(int appWidgetId, int dateIndex) {
            set(sharedPreferences, appWidgetId, dateIndex);
        }

        public static void remove(SharedPreferences sharedPreferences, int appWidgetId) {
            sharedPreferences.edit().remove(getKey(appWidgetId)).commit();
        }

        public static void remove(Context context, int appWidgetId) {
            remove(getSharedPreferences(context), appWidgetId);
        }

        public void remove(int appWidgetId) {
            remove(sharedPreferences, appWidgetId);
        }

        public static void clear(SharedPreferences sharedPreferences) {
            sharedPreferences.edit().clear().commit();
        }

        public static void clear(Context context) {
            clear(getSharedPreferences(context));
        }

        public void clear() {
            clear(sharedPreferences);
        }
    }

    // Cached information

    private static class CachedInfoHolder {
        Calendar date;
        SortedSet<Task> timetable;
    }

    private static CachedInfoHolder[] cachedInfo = new CachedInfoHolder[DATE_NUMBER_MAXIMUM];

    void updateCachedInfo(Context context, boolean forceUpdate) {

        if (forceUpdate || cachedInfo[0] == null || !TimeUtils.isToday(cachedInfo[DATE_NUMBER_MAXIMUM / 2].date)) {

            // Cache the starting date.
            Calendar dateStart = TimeUtils.getDate();
            dateStart.add(Calendar.DAY_OF_YEAR, - DATE_NUMBER_MAXIMUM / 2);

            // Cache TimetableManager.
            TimetableManager timetableManager = TimetableManager.getInstance(context.getApplicationContext());

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
                cachedInfo[i].timetable = timetableManager.getTimetable(cachedInfo[i].date);
            }
        }
    }

    private PendingIntent getDateChangedPendingIntent(Context context, int appWidgetId, int dateDelta) {
        Intent dateChangedIntent = new Intent(context, this.getClass());
        dateChangedIntent.setAction(ACTION_DATE_CHANGED);
        dateChangedIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        dateChangedIntent.putExtra(EXTRA_DATE_DELTA, dateDelta);
        // Using requestCode to differentiate pendingIntents.
        return PendingIntent.getBroadcast(context, appWidgetId << 16 + dateDelta, dateChangedIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setTaskViewColor(RemoteViews taskViews, Context context, int color) {
        taskViews.setTextColor(R.id.appwidget_timetable_task_name, context.getResources().getColor(color));
        taskViews.setTextColor(R.id.appwidget_timetable_task_detail, context.getResources().getColor(color));
        taskViews.setTextColor(R.id.appwidget_timetable_task_time_start, context.getResources().getColor(color));
        taskViews.setTextColor(R.id.appwidget_timetable_task_time_end, context.getResources().getColor(color));
    }

    private void buildUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews appWidgetViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable);

        // Remove sub views in case the host recycles the view, while this also removes the loading TextView.
        appWidgetViews.removeAllViews(R.id.appwidget_timetable_date_list);
        appWidgetViews.removeAllViews(R.id.appwidget_timetable_task_list);

        // Add subviews.
        RemoteViews subViews;

        // Cache the date indexes.
        int dateIndex = DateIndexManager.get(context, appWidgetId);
        int dateShownIndexStart = dateIndex - DATE_NUMBER_SHOWN / 2;

        // Set text color and PendingIntent for date navigation view.
        //
        // Canceling PendingIntent in case the view is reused causes exception, setBoolean with setClickable
        // causes exception, set Boolean with setEnabled does nothing; don't know how to disable it.
        // Dealing with this in onDateChanged.
        if (dateIndex != 0) {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_navigation_left, context.getResources().getColor(R.color.white));
            appWidgetViews.setOnClickPendingIntent(R.id.appwidget_timetable_date_navigation_left, getDateChangedPendingIntent(context, appWidgetId, -1));
        } else {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_navigation_left, context.getResources().getColor(R.color.white_fading));
        }
        if (dateIndex != DATE_NUMBER_MAXIMUM - 1) {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_navigation_right, context.getResources().getColor(R.color.white));
            appWidgetViews.setOnClickPendingIntent(R.id.appwidget_timetable_date_navigation_right, getDateChangedPendingIntent(context, appWidgetId, 1));
        } else {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_navigation_right, context.getResources().getColor(R.color.white_fading));
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
                    subViews.setOnClickPendingIntent(R.id.appwidget_timetable_date, getDateChangedPendingIntent(context, appWidgetId, i - DATE_NUMBER_SHOWN / 2));
                }
            }

            appWidgetViews.addView(R.id.appwidget_timetable_date_list, subViews);
        }

        // Add tasks to view.
        SortedSet<Task> timetable = cachedInfo[dateIndex].timetable;

        if (timetable.size() == 0) {

            // Add no-task view.
            subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_notask);
            appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

        } else {

            // Add the first divider.
            // TODO: Check if this should be marked active.
            subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task_list_divider);
            subViews.setImageViewResource(R.id.appwidget_timetable_task_list_divider, R.drawable.appwidget_timetable_task_list_divider_normal);
            appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

            // Add task views.
            Iterator<Task> iter = timetable.iterator();
            Calendar now = TimeUtils.getNow();

            // Auto-scroll the list to show more tasks.
            Iterator<Task> iter2 = timetable.iterator();
            while (timetable.size() > TASK_NUMBER_SHOWN && iter2.next().getEndTime().before(now)) {
                iter.next();
            }

            for (int i = 0; i != TASK_NUMBER_SHOWN; ++i) {

                if (iter.hasNext()) {

                    Task task = iter.next();

                    // Add task view.
                    subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task);
                    // Add content for task view.
                    subViews.setTextViewText(R.id.appwidget_timetable_task_name, task.getName());
                    subViews.setTextViewText(R.id.appwidget_timetable_task_detail, task.getDetail());
                    subViews.setTextViewText(R.id.appwidget_timetable_task_time_start, TimeUtils.getHourMinuteString(task.getStartTime()));
                    subViews.setTextViewText(R.id.appwidget_timetable_task_time_end, TimeUtils.getHourMinuteString(task.getEndTime()));
                    // Set color for today's task view.
                    // Android Studio prompts incorrectly at present; We just pass color id to our private function and resolve it inside.
                    if (dateIndex == DATE_NUMBER_MAXIMUM / 2) {
                        if (task.getEndTime().before(now)) {
                            // Finished
                            setTaskViewColor(subViews, context, R.color.grey);
                        } else if (task.getStartTime().after(now)) {
                            // Scheduled
                            setTaskViewColor(subViews, context, R.color.grey_dark);
                        } else {
                            // Active
                            setTaskViewColor(subViews, context, R.color.blue_light);
                        }
                    } else {
                        // Default
                        setTaskViewColor(subViews, context, R.color.grey_dark);
                    }
                    // Add to appWidgetViews.
                    appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

                    // Add divider.
                    subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task_list_divider);
                    subViews.setImageViewResource(R.id.appwidget_timetable_task_list_divider, R.drawable.appwidget_timetable_task_list_divider_normal);
                    appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

                } else {

                    // Add empty views for spacing in LinearLayout.

                    // Add default (empty) task view.
                    subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task);
                    appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

                    // Add transparent divider.
                    subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task_list_divider);
                    subViews.setImageViewResource(R.id.appwidget_timetable_task_list_divider, R.drawable.appwidget_timetable_task_list_divider_transparent);
                    appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);
                }
            }
        }

        // Update AppWidget.
        appWidgetManager.updateAppWidget(appWidgetId, appWidgetViews);
        LogHelper.i("appWidgetId: " + appWidgetId);
        LogHelper.i("dateIndex:" + dateIndex);
    }

    private void onDateChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int dateDelta) {

        LogHelper.i("appWidgetId: " + appWidgetId);

        DateIndexManager dateIndexManager = new DateIndexManager(context);

        int dateIndex = dateIndexManager.get(appWidgetId);

        // Ignore invalid date changes.
        if (dateIndex + dateDelta < 0 || dateIndex + dateDelta > DATE_NUMBER_MAXIMUM - 1) {
            LogHelper.i("Ignoring intent for appWidgetId: " + appWidgetId);
            return;
        }

        dateIndexManager.set(appWidgetId, dateIndex + dateDelta);

        buildUpdate(context, appWidgetManager, appWidgetId);
    }

    // TODO: Receive data update broadcast
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (ACTION_DATE_CHANGED.equals(action)) {
            onDateChanged(context, AppWidgetManager.getInstance(context), intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID), intent.getIntExtra(EXTRA_DATE_DELTA, 0));
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

        DateIndexManager.clear(context);

        super.onDisabled(context);
    }

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

        DateIndexManager dateIndexManager = new DateIndexManager(context);

        // Remove date index for deleted AppWidgets
        for (int appWidgetId : appWidgetIds) {
            dateIndexManager.remove(appWidgetId);
        }

        super.onDeleted(context, appWidgetIds);
    }
}
