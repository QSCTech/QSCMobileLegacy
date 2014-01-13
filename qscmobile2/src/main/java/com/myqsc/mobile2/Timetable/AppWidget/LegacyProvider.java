package com.myqsc.mobile2.Timetable.AppWidget;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.Timetable.Information.Task;
import com.myqsc.mobile2.Timetable.Information.TimetableManager;
import com.myqsc.mobile2.Utility.TimeUtils;

import java.util.Calendar;
import java.util.Iterator;
import java.util.SortedSet;

public abstract class LegacyProvider extends AppWidgetProvider {

    // Actions

    private static final String ACTION_DATE_INDEX_CHANGED = "com.myqsc.mobile2.Timetable.AppWidget.DATE_INDEX_CHANGED";

    private static final String ACTION_UPDATE_TODAY = "com.myqsc.mobile2.Timetable.AppWidget.UPDATE_TODAY";

    private static final String ACTION_UPDATE_DATE = "com.myqsc.mobile2.Timetable.AppWidget.UPDATE_DATE";

    // Intent extras

    private static final String EXTRA_DATE_INDEX = "DATE_INDEX";

    // Constants for displaying information

    private static final int DATE_COUNT = 7;

    private static final int DATE_INDEX_TODAY = 3;

    // Delay time for update today

    private static final int UPDATE_ALARM_DELAY_MILLISECOND = 1000;

    // AppWidget display parameters, supplied by subclasses.

    protected abstract int getDisplayedDateCount();

    protected abstract int getDisplayedTaskCount();

    // Date index for each AppWidget stored in DateIndexManager.

    private DateIndexManager getDateIndexManager(Context context) {
        return DateIndexManager.getInstance(context, this.getClass(), DATE_INDEX_TODAY);
    }

    private Calendar getDate(int dateIndex) {

        Calendar date = TimeUtils.getDate();

        if (dateIndex != DATE_INDEX_TODAY) {
            date.add(Calendar.DAY_OF_YEAR, dateIndex - DATE_INDEX_TODAY);
        }

        return date;
    }

    // Build and update AppWidget views.

    // Get weekday strings for display in AppWidget.
    private String getWeekdayString(Context context, Calendar date) {
        switch (date.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return context.getResources().getString(R.string.timetable_appwidget_date_monday);
            case Calendar.TUESDAY:
                return context.getResources().getString(R.string.timetable_appwidget_date_tuesday);
            case Calendar.WEDNESDAY:
                return context.getResources().getString(R.string.timetable_appwidget_date_wednesday);
            case Calendar.THURSDAY:
                return context.getResources().getString(R.string.timetable_appwidget_date_thursday);
            case Calendar.FRIDAY:
                return context.getResources().getString(R.string.timetable_appwidget_date_friday);
            case Calendar.SATURDAY:
                return context.getResources().getString(R.string.timetable_appwidget_date_saturday);
            case Calendar.SUNDAY:
                return context.getResources().getString(R.string.timetable_appwidget_date_sunday);
            default:
                return null;
        }
    }

    // NOTICE: Always use this.getClass() when building intents in case there are subclasses.

    private PendingIntent makeDateChangedPendingIntent(Context context, int appWidgetId, int dateIndex) {
        Intent dateChangedIntent = new Intent(context, this.getClass());
        dateChangedIntent.setAction(ACTION_DATE_INDEX_CHANGED);
        dateChangedIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        dateChangedIntent.putExtra(EXTRA_DATE_INDEX, dateIndex);
        // Using requestCode to differentiate pendingIntents.
        return PendingIntent.getBroadcast(context, appWidgetId << 16 + dateIndex, dateChangedIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setTaskViewColor(RemoteViews taskViews, Context context, int color) {
        taskViews.setTextColor(R.id.appwidget_timetable_task_name, context.getResources().getColor(color));
        taskViews.setTextColor(R.id.appwidget_timetable_task_detail, context.getResources().getColor(color));
        taskViews.setTextColor(R.id.appwidget_timetable_task_time_start, context.getResources().getColor(color));
        taskViews.setTextColor(R.id.appwidget_timetable_task_time_end, context.getResources().getColor(color));
    }

    // NOTE: Color passed to setTaskViewColor() will be resolved there.
    @SuppressLint("ResourceAsColor")
    private void buildUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Get the display specification.
        int displayedDateCount = getDisplayedDateCount();
        int displayedTaskCount = getDisplayedTaskCount();

        // Build AppWidgetView.
        RemoteViews appWidgetViews = new RemoteViews(context.getPackageName(), R.layout.timetable_appwidget_legacy);

        // Remove sub views in case the host recycles the view, while this also removes the loading TextView.
        appWidgetViews.removeAllViews(R.id.appwidget_timetable_date_list);
        appWidgetViews.removeAllViews(R.id.appwidget_timetable_task_list);

        // Add subviews.
        RemoteViews subViews;

        // Cache the date indexes.
        int dateIndex = getDateIndexManager(context).get(appWidgetId);
        int dateShownIndexStart = dateIndex - displayedDateCount / 2;

        // Set text color and PendingIntent for date navigation view.
        // Canceling PendingIntent in case the view is reused causes exception in host.
        // Filtering in onDateIndexChanged().
        if (dateIndex != 0) {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_backward, context.getResources().getColor(R.color.white));
            appWidgetViews.setOnClickPendingIntent(R.id.appwidget_timetable_date_backward, makeDateChangedPendingIntent(context, appWidgetId, dateIndex - 1));
        } else {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_backward, context.getResources().getColor(R.color.white_fading));
            //makeDateChangedPendingIntent(context, appWidgetId, 0).cancel();
        }
        if (dateIndex != DATE_COUNT - 1) {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_forward, context.getResources().getColor(R.color.white));
            appWidgetViews.setOnClickPendingIntent(R.id.appwidget_timetable_date_forward, makeDateChangedPendingIntent(context, appWidgetId, dateIndex + 1));
        } else {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_forward, context.getResources().getColor(R.color.white_fading));
            //makeDateChangedPendingIntent(context, appWidgetId, DATE_COUNT - 1).cancel();
        }

        // Add dates to view.
        for (int i = 0; i != displayedDateCount; ++i) {

            subViews = new RemoteViews(context.getPackageName(), R.layout.timetable_appwidget_legacy_date);

            // Determine whether the date view should be empty or not.
            if (dateShownIndexStart + i >= 0 && dateShownIndexStart + i <= DATE_COUNT - 1) {

                // Add content to the date view.
                if (dateShownIndexStart + i == DATE_INDEX_TODAY) {
                    subViews.setTextViewText(R.id.appwidget_timetable_date, context.getString(R.string.timetable_appwidget_date_today));
                } else {
                    subViews.setTextViewText(R.id.appwidget_timetable_date, getWeekdayString(context, getDate(dateShownIndexStart + i)));
                }
                if (i == displayedDateCount / 2) {
                    subViews.setTextColor(R.id.appwidget_timetable_date, context.getResources().getColor(R.color.white));
                } else {
                    subViews.setTextColor(R.id.appwidget_timetable_date, context.getResources().getColor(R.color.white_fading));
                    subViews.setOnClickPendingIntent(R.id.appwidget_timetable_date, makeDateChangedPendingIntent(context, appWidgetId, dateShownIndexStart + i));
                }
            }

            appWidgetViews.addView(R.id.appwidget_timetable_date_list, subViews);
        }

        // Add tasks to view.
        SortedSet<Task> timetable = TimetableManager.getInstance(context).getTimetable(getDate(dateIndex));

        if (timetable.size() == 0) {

            // Add no-task view.
            subViews = new RemoteViews(context.getPackageName(), R.layout.timetable_appwidget_legacy_notask);
            appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

        } else {

            // Add the first divider.
            subViews = new RemoteViews(context.getPackageName(), R.layout.timetable_appwidget_legacy_task_list_divider);
            subViews.setImageViewResource(R.id.appwidget_timetable_task_list_divider, R.drawable.appwidget_timetable_task_list_divider_normal);
            appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

            // Add task views.
            Iterator<Task> iter = timetable.iterator();
            Calendar now = TimeUtils.getNow();

            // Auto-scroll the list to show more tasks if today.
            if (dateIndex == DATE_INDEX_TODAY) {
                Iterator<Task> iter2 = timetable.iterator();
                int start = 0;
                while (timetable.size() - start > displayedTaskCount && iter2.next().getEndTime().before(now)) {
                    iter.next();
                    ++start;
                }
            }

            for (int i = 0; i != displayedTaskCount; ++i) {

                if (iter.hasNext()) {

                    Task task = iter.next();

                    // Add task view.
                    subViews = new RemoteViews(context.getPackageName(), R.layout.timetable_appwidget_legacy_task);
                    // Add content for task view.
                    subViews.setTextViewText(R.id.appwidget_timetable_task_name, task.getName());
                    subViews.setTextViewText(R.id.appwidget_timetable_task_detail, task.getDetail());
                    subViews.setTextViewText(R.id.appwidget_timetable_task_time_start, TimeUtils.getHourMinuteString(task.getStartTime()));
                    subViews.setTextViewText(R.id.appwidget_timetable_task_time_end, TimeUtils.getHourMinuteString(task.getEndTime()));
                    // Set color for today's task view.
                    // Android Studio prompts incorrectly at present; We just pass color id to our private function and resolve it inside.
                    if (dateIndex == DATE_INDEX_TODAY) {
                        if (task.getEndTime().before(now)) {
                            // Finished
                            setTaskViewColor(subViews, context, R.color.grey_medium);
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
                    subViews = new RemoteViews(context.getPackageName(), R.layout.timetable_appwidget_legacy_task_list_divider);
                    subViews.setImageViewResource(R.id.appwidget_timetable_task_list_divider, R.drawable.appwidget_timetable_task_list_divider_normal);
                    appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

                } else {

                    // Add empty views for spacing in LinearLayout.

                    // Add default (empty) task view.
                    subViews = new RemoteViews(context.getPackageName(), R.layout.timetable_appwidget_legacy_task);
                    appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

                    // Add transparent divider.
                    subViews = new RemoteViews(context.getPackageName(), R.layout.timetable_appwidget_legacy_task_list_divider);
                    subViews.setImageViewResource(R.id.appwidget_timetable_task_list_divider, R.drawable.appwidget_timetable_task_list_divider_transparent);
                    appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);
                }
            }
        }

        // Update AppWidget.
        appWidgetManager.updateAppWidget(appWidgetId, appWidgetViews);
    }

    // Manage alarms.

    // Returns null if no update is needed.
    private Calendar getUpdateTime(Context context) {

        // Cache now, set the initial value of updateTime to null.
        Calendar now = TimeUtils.getNow(), updateTime = null;

        // Cache the timetable.
        SortedSet<Task> timetable = TimetableManager.getInstance(context).getTimetable(TimeUtils.getDate());

        // Traverse the timetable if not empty.
        if (!timetable.isEmpty()) {
            Iterator<Task> iter = timetable.iterator();
            Task task;
            Calendar time;
            while (iter.hasNext()) {
                task = iter.next();
                time = task.getStartTime();
                if (time.after(now) && (updateTime == null || time.before(updateTime))) {
                    updateTime = time;
                }
                time = task.getEndTime();
                if (time.after(now) && (updateTime == null || time.before(updateTime))) {
                    updateTime = time;
                }
            }
        }

        return updateTime;
    }

    // Set or cancel the update today alarm.
    // NOTICE: Should be called whenever the DateIndex of any AppWidget is changed or the timetable of today is changed.
    private void updateUpdateTodayAlarm(Context context) {

        // Build the intent and some others.
        Intent updateTodayIntent = new Intent(context, this.getClass());
        updateTodayIntent.setAction(ACTION_UPDATE_TODAY);
        PendingIntent updateTodayPendingIntent;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Check if the alarm should be set.
        int[] idsShowingToday = getDateIndexManager(context).getIdsShowingToday();
        if (idsShowingToday.length != 0) {
            Calendar updateTime = getUpdateTime(context);
            if (updateTime != null) {

                // Set the alarm and return.
                updateTodayIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idsShowingToday);
                updateTodayPendingIntent = PendingIntent.getBroadcast(context, 0, updateTodayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC, updateTime.getTimeInMillis() + UPDATE_ALARM_DELAY_MILLISECOND, updateTodayPendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC, updateTime.getTimeInMillis() + UPDATE_ALARM_DELAY_MILLISECOND, updateTodayPendingIntent);
                }
                return;
            }
        }

        // Cancel the alarm
        updateTodayPendingIntent = PendingIntent.getBroadcast(context, 0, updateTodayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(updateTodayPendingIntent);
    }

    // Set the update date alarm.
    private void setUpdateDateAlarm(Context context) {

        Intent updateDateIntent = new Intent(context, this.getClass());
        updateDateIntent.setAction(ACTION_UPDATE_DATE);

        PendingIntent updateDatePendingIntent = PendingIntent.getBroadcast(context, 0, updateDateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar updateTime = TimeUtils.getDate();
        updateTime.add(Calendar.DAY_OF_YEAR, 1);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC, updateTime.getTimeInMillis() + UPDATE_ALARM_DELAY_MILLISECOND, updateDatePendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC, updateTime.getTimeInMillis() + UPDATE_ALARM_DELAY_MILLISECOND, updateDatePendingIntent);
        }
    }

    // Cancel the update date alarm.
    private void cancelUpdateDateAlarm(Context context) {

        Intent updateDateIntent = new Intent(context, this.getClass());
        updateDateIntent.setAction(ACTION_UPDATE_DATE);

        PendingIntent updateDatePendingIntent = PendingIntent.getBroadcast(context, 0, updateDateIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(updateDatePendingIntent);
    }

    // Handle actions.

    private void onDateIndexChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int dateIndex) {

        // Handle date index change.
        DateIndexManager dateIndexManager = getDateIndexManager(context);

        // Filter invalid intents cause by the reuse of view.
        if (dateIndex == dateIndexManager.get(appWidgetId)) {
            return;
        }

        // Change date index.
        getDateIndexManager(context).set(appWidgetId, dateIndex);

        // Update view.
        buildUpdate(context, appWidgetManager, appWidgetId);

        // Update update today alarm because the date index has changed.
        updateUpdateTodayAlarm(context);

        // Ensure that update date alarm is set.
        setUpdateDateAlarm(context);
    }

    private void onUpdateToday(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Update every AppWidget given.
        for (int appWidgetId : appWidgetIds) {
            buildUpdate(context, appWidgetManager, appWidgetId);
        }

        // Update update today alarm.
        updateUpdateTodayAlarm(context);

        // Ensure that update date alarm is set.
        setUpdateDateAlarm(context);
    }

    private void onUpdateDate(Context context, AppWidgetManager appWidgetManager) {

        // Update every AppWidget.
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, this.getClass()));
        for (int appWidgetId : appWidgetIds) {
            buildUpdate(context, appWidgetManager, appWidgetId);
        }

        // Set update date alarm for the next update.
        setUpdateDateAlarm(context);

        // Update update today alarm because the timetable for today has changed.
        updateUpdateTodayAlarm(context);
    }

    @Override
    public void onDisabled(Context context) {

        // Clear date index storage.
        getDateIndexManager(context).clear();

        // Cancel update date alarm.
        cancelUpdateDateAlarm(context);

        super.onDisabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Update every AppWidget given.
        for (int appWidgetId : appWidgetIds) {
            buildUpdate(context, appWidgetManager, appWidgetId);
        }

        // Update update today alarm because instances with new date indexes has been added.
        updateUpdateTodayAlarm(context);

        // Ensure that update date alarm is set.
        setUpdateDateAlarm(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

        DateIndexManager dateIndexManager = getDateIndexManager(context);

        // Remove date indexes for deleted AppWidgets
        for (int appWidgetId : appWidgetIds) {
            dateIndexManager.remove(appWidgetId);
        }

        // Update update today alarm because instances with date indexes has been removed.
        updateUpdateTodayAlarm(context);

        super.onDeleted(context, appWidgetIds);
    }

    // TODO: Handle TimetableManager update broadcast.
    // TODO: Handle system date changed.
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (ACTION_DATE_INDEX_CHANGED.equals(action)) {
            onDateIndexChanged(context, AppWidgetManager.getInstance(context), intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID), intent.getIntExtra(EXTRA_DATE_INDEX, 0));
        } else if (ACTION_UPDATE_TODAY.equals(action)) {
            onUpdateToday(context, AppWidgetManager.getInstance(context), intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS));
        } else if (ACTION_UPDATE_DATE.equals(action)) {
            onUpdateDate(context, AppWidgetManager.getInstance(context));
        } else {
            super.onReceive(context, intent);
        }
    }
}
