package com.myqsc.mobile2.Timetable.AppWidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

    private static final String ACTION_UPDATE_TODAY = "com.myqsc.mobile2.Timetable.AppWidget.UPDATE_TODAY";

    private static final String ACTION_UPDATE_DATE = "com.myqsc.mobile2.Timetable.AppWidget.UPDATE_DATE";

    // Intent extras

    private static final String EXTRA_DATE_DELTA = "DATE_DELTA";

    // Constants for displaying information

    private static final int DATE_NUMBER_MAXIMUM = 7;

    private static final int DATE_INDEX_TODAY = 3;

    private static final int DATE_INDEX_INVALID = -1;

    private static final int DATE_NUMBER_SHOWN = 3;

    private static final int TASK_NUMBER_SHOWN = 5;

    // Delay time for update today

    private static final int UPDATE_ALARM_DELAY_MILLISECOND = 1000;

    // Store date index for each AppWidget in SharedPreferences and access via DateIndexManager.

    private static class DateIndexManager {

        private static final String PREFERENCES_FILE_NAME = "com.myqsc.mobile2.Timetable.AppWidget";

        private static String getKey(int appWidgetId) {
            return appWidgetId + ".DateIndex";
        }

        private static final String IDS_SHOWING_TODAY = "IdsShowingToday";

        private static final String IDS_SEPARATOR = ",";

        SharedPreferences sharedPreferences;

        DateIndexManager(Context context) {
            this.sharedPreferences = getSharedPreferences(context);
        }

        private static SharedPreferences getSharedPreferences(Context context) {
            return context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        }

        // Store IDs of AppWidgets that are showing today.
        // AppWidgetManager.getAppWidgetIds() returns all the ids that have been added historically.

        private static void addToIds(SharedPreferences sharedPreferences, int id) {
            String idsPrevious = sharedPreferences.getString(IDS_SHOWING_TODAY, "");
            // REMOVEME
            if (idsPrevious.equals("0")) {
                LogHelper.i("Remove 0~");
                idsPrevious = "";
            }

            StringBuilder idsBuilder = new StringBuilder(idsPrevious);
            if (idsPrevious.length() != 0) {
                idsBuilder.append(IDS_SEPARATOR);
            }
            idsBuilder.append(Integer.toString(id));
            LogHelper.i(idsBuilder.toString());
            sharedPreferences.edit().putString(IDS_SHOWING_TODAY, idsBuilder.toString()).commit();
        }

        private static void removeFromIds(SharedPreferences sharedPreferences, int id) {
            String idsPrevious =  sharedPreferences.getString(IDS_SHOWING_TODAY, "");
            LogHelper.i(idsPrevious);
            if (idsPrevious.length() == 0) {
                return;
            }
            StringBuilder idsBuilder = new StringBuilder();
            String idString = Integer.toString(id);
            boolean first = true;
            for (String idPrevious : idsPrevious.split(IDS_SEPARATOR)) {
                if (!idPrevious.equals(idString)) {
                    if (first) {
                        first = false;
                    } else {
                        idsBuilder.append(IDS_SEPARATOR);
                    }
                    idsBuilder.append(idPrevious);
                }
            }
            LogHelper.i(idsBuilder.toString());
            sharedPreferences.edit().putString(IDS_SHOWING_TODAY, idsBuilder.toString()).commit();
        }

        // Update appWidgetIdsShowingToday when date index is changed.
        private static void onChanged(SharedPreferences sharedPreferences, int appWidgetId, int dateIndexPrevious, int dateIndex) {
            LogHelper.i("dateIndexPrevious: " + dateIndexPrevious + "; dateIndex: " + dateIndex);
            if (dateIndexPrevious == DATE_INDEX_TODAY) {
                removeFromIds(sharedPreferences, appWidgetId);
            } else if (dateIndex == DATE_INDEX_TODAY) {
                addToIds(sharedPreferences, appWidgetId);
            }
        }

        private static void onRemoved(SharedPreferences sharedPreferences, int appWidgetId) {
            removeFromIds(sharedPreferences, appWidgetId);
        }

        public static int[] getIdsShowingToday(SharedPreferences sharedPreferences) {
            String[] idStrings = sharedPreferences.getString(IDS_SHOWING_TODAY, "").split(IDS_SEPARATOR);
            int[] ids;
            if (idStrings[0].length() != 0) {
                ids = new int[idStrings.length];
                for (int i = 0; i != idStrings.length; ++i) {
                    ids[i] = Integer.valueOf(idStrings[i]);
                }
            } else {
                ids = new int[0];
            }
            return ids;
        }

        public static int[] getIdsShowingToday(Context context) {
            return getIdsShowingToday(getSharedPreferences(context));
        }

        public int[] getIdsShowingToday() {
            return getIdsShowingToday(sharedPreferences);
        }

        // Return and set to default value (DATE_INDEX_TODAY) if the date index has not been set.
        private static int get(SharedPreferences sharedPreferences, int appWidgetId) {
            String key = getKey(appWidgetId);
            int dateIndex = sharedPreferences.getInt(key, DATE_INDEX_INVALID);
            if (dateIndex == DATE_INDEX_INVALID) {
                dateIndex = DATE_INDEX_TODAY;
                sharedPreferences.edit().putInt(key, dateIndex).commit();
                onChanged(sharedPreferences, appWidgetId, DATE_INDEX_INVALID, dateIndex);
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
            int dateIndexPrevious = sharedPreferences.getInt(getKey(appWidgetId), DATE_INDEX_INVALID);
            sharedPreferences.edit().putInt(getKey(appWidgetId), dateIndex).commit();
            if (dateIndex != dateIndexPrevious) {
                onChanged(sharedPreferences, appWidgetId, dateIndexPrevious, dateIndex);
            }
        }

        public static void set(Context context, int appWidgetId, int dateIndex) {
            set(getSharedPreferences(context), appWidgetId, dateIndex);
        }

        public void set(int appWidgetId, int dateIndex) {
            set(sharedPreferences, appWidgetId, dateIndex);
        }

        public static void remove(SharedPreferences sharedPreferences, int appWidgetId) {
            sharedPreferences.edit().remove(getKey(appWidgetId)).commit();
            onRemoved(sharedPreferences, appWidgetId);
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
    // TODO: Make this a class and differentiate update() and ensureData().

    private static class CachedInfoHolder {
        Calendar date;
        SortedSet<Task> timetable;
    }

    private static CachedInfoHolder[] cachedInfo = new CachedInfoHolder[DATE_NUMBER_MAXIMUM];

    private void updateCachedInfo(Context context, boolean forceUpdate) {

        if (forceUpdate || cachedInfo[0] == null || !TimeUtils.isToday(cachedInfo[DATE_INDEX_TODAY].date)) {

            // Cache the starting date.
            Calendar dateStart = TimeUtils.getDate();
            dateStart.add(Calendar.DAY_OF_YEAR, - DATE_INDEX_TODAY);

            // Cache TimetableManager.
            TimetableManager timetableManager = TimetableManager.getInstance(context);

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

    private PendingIntent makeDateChangedPendingIntent(Context context, int appWidgetId, int dateDelta) {
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

        // Check if the cached info should be updated.
        updateCachedInfo(context, false);

        // Build AppWidgetView.
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
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_backward, context.getResources().getColor(R.color.white));
            appWidgetViews.setOnClickPendingIntent(R.id.appwidget_timetable_date_backward, makeDateChangedPendingIntent(context, appWidgetId, -1));
        } else {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_backward, context.getResources().getColor(R.color.white_fading));
        }
        if (dateIndex != DATE_NUMBER_MAXIMUM - 1) {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_forward, context.getResources().getColor(R.color.white));
            appWidgetViews.setOnClickPendingIntent(R.id.appwidget_timetable_date_forward, makeDateChangedPendingIntent(context, appWidgetId, 1));
        } else {
            appWidgetViews.setTextColor(R.id.appwidget_timetable_date_forward, context.getResources().getColor(R.color.white_fading));
        }

        // Add dates to view.
        for (int i = 0; i != DATE_NUMBER_SHOWN; ++i) {

            subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_date);

            // Determine whether the date view should be empty or not.
            if (dateShownIndexStart + i >= 0 && dateShownIndexStart + i <= DATE_NUMBER_MAXIMUM - 1) {

                // Add content to the date view.
                if (dateShownIndexStart + i == DATE_INDEX_TODAY) {
                    subViews.setTextViewText(R.id.appwidget_timetable_date, context.getString(R.string.appwidget_timetable_today));
                } else {
                    subViews.setTextViewText(R.id.appwidget_timetable_date, TimeUtils.getWeekdayString(cachedInfo[dateShownIndexStart + i].date));
                }
                if (i == DATE_NUMBER_SHOWN / 2) {
                    subViews.setTextColor(R.id.appwidget_timetable_date, context.getResources().getColor(R.color.white));
                } else {
                    subViews.setTextColor(R.id.appwidget_timetable_date, context.getResources().getColor(R.color.white_fading));
                    subViews.setOnClickPendingIntent(R.id.appwidget_timetable_date, makeDateChangedPendingIntent(context, appWidgetId, i - DATE_NUMBER_SHOWN / 2));
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
            subViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable_task_list_divider);
            subViews.setImageViewResource(R.id.appwidget_timetable_task_list_divider, R.drawable.appwidget_timetable_task_list_divider_normal);
            appWidgetViews.addView(R.id.appwidget_timetable_task_list, subViews);

            // Add task views.
            Iterator<Task> iter = timetable.iterator();
            Calendar now = TimeUtils.getNow();

            // Auto-scroll the list to show more tasks.
            Iterator<Task> iter2 = timetable.iterator();
            int start = 0;
            while (timetable.size() - start > TASK_NUMBER_SHOWN && iter2.next().getEndTime().before(now)) {
                iter.next();
                ++start;
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

    // Returns null if no update is needed.
    private Calendar getUpdateTime(Context context) {

        // Cache now, set the initial value of updateTime to null.
        Calendar now = TimeUtils.getNow(), updateTime = null;

        // Cache the timetable.
        updateCachedInfo(context, false);
        SortedSet<Task> timetable = cachedInfo[DATE_INDEX_TODAY].timetable;

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
    private void updateUpdateTodayAlarm(Context context) {

        // Build the intent and some others.
        Intent updateTodayIntent = new Intent(context, this.getClass());
        updateTodayIntent.setAction(ACTION_UPDATE_TODAY);
        PendingIntent updateTodayPendingIntent;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Check if the alarm should be set.
        int[] idsShowingToday = DateIndexManager.getIdsShowingToday(context);
        if (idsShowingToday.length != 0) {
            Calendar updateTime = getUpdateTime(context);
            if (updateTime != null) {

                // Set the alarm and return.
                LogHelper.i(Integer.toString(idsShowingToday[0]));
                LogHelper.i(updateTime.toString());
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
        LogHelper.i("Cancel update today alarm.");
        updateTodayPendingIntent = PendingIntent.getBroadcast(context, 0, updateTodayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(updateTodayPendingIntent);
    }

    // Set the update date alarm.
    private void setUpdateDateAlarm(Context context) {

        LogHelper.i("Set update date alarm");

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

        // Update view.
        buildUpdate(context, appWidgetManager, appWidgetId);

        // Update update today alarm.
        updateUpdateTodayAlarm(context);
    }

    private void onUpdateToday(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Update every AppWidget given.
        for (int appWidgetId : appWidgetIds) {
            buildUpdate(context, appWidgetManager, appWidgetId);
        }

        // Update update today alarm.
        updateUpdateTodayAlarm(context);
    }

    private void onUpdateDate(Context context, AppWidgetManager appWidgetManager) {

        LogHelper.i("");

        // TODO: Update cached info for date change. (May be needed when the cached info is refactored)
        //updateCachedInfo(context, false);

        // Update every AppWidget.
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, this.getClass()));
        for (int appWidgetId : appWidgetIds) {
            buildUpdate(context, appWidgetManager, appWidgetId);
        }

        // Set update date alarm for the next update.
        setUpdateDateAlarm(context);

        // Update update today alarm.
        updateUpdateTodayAlarm(context);
    }

    // TODO: Receive data update broadcast
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (ACTION_DATE_CHANGED.equals(action)) {
            onDateChanged(context, AppWidgetManager.getInstance(context), intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID), intent.getIntExtra(EXTRA_DATE_DELTA, 0));
        } else if (ACTION_UPDATE_TODAY.equals(action)) {
            onUpdateToday(context, AppWidgetManager.getInstance(context), intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS));
        } else if (ACTION_UPDATE_DATE.equals(action)) {
            onUpdateDate(context, AppWidgetManager.getInstance(context));
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

        // Update every AppWidget given.
        for (int appWidgetId : appWidgetIds) {
            LogHelper.i("appWidgetId: " + appWidgetId);
            buildUpdate(context, appWidgetManager, appWidgetId);
        }

        // Update update today alarm.
        updateUpdateTodayAlarm(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

        DateIndexManager dateIndexManager = new DateIndexManager(context);

        // Remove date index for deleted AppWidgets
        for (int appWidgetId : appWidgetIds) {
            dateIndexManager.remove(appWidgetId);
        }

        // Update update today alarm.
        updateUpdateTodayAlarm(context);

        super.onDeleted(context, appWidgetIds);
    }
}
