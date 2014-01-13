package com.myqsc.mobile2.Timetable.AppWidget;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

class DateIndexManager {

    private static final String PREFERENCES_FILE_NAME = "Timetable.AppWidget.DateIndex";

    private static final int DATE_INDEX_INVALID = -1;

    private static final String IDS_SHOWING_TODAY = "IdsShowingToday";

    private static final String IDS_SEPARATOR = ",";

    private String keyPrefix;

    private String idsShowingTodayKey;

    private int dateIndexToday;

    SharedPreferences sharedPreferences;

    private static WeakHashMap<Class<? extends AppWidgetProvider>, WeakReference<DateIndexManager>> instanceCache = new WeakHashMap<Class<? extends AppWidgetProvider>, WeakReference<DateIndexManager>>();


    public static DateIndexManager getInstance(Context context, Class<? extends AppWidgetProvider> appWidgetProviderClass, int dateIndexToday) {

        synchronized (instanceCache) {

            WeakReference<DateIndexManager> weakRef = instanceCache.get(appWidgetProviderClass);
            DateIndexManager instance = null;

            if (weakRef != null) {
                instance = weakRef.get();
            }

            if (instance == null) {
                instance = new DateIndexManager(context, appWidgetProviderClass, dateIndexToday);
                instanceCache.put(appWidgetProviderClass, new WeakReference<DateIndexManager>(instance));
            }

            return instance;
        }
    }

    private DateIndexManager(Context context, Class<? extends AppWidgetProvider> appWidgetProviderClass, int dateIndexToday) {
        keyPrefix = appWidgetProviderClass.getSimpleName();
        idsShowingTodayKey = keyPrefix + IDS_SHOWING_TODAY;
        sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        this.dateIndexToday = dateIndexToday;
    }

    private String getKey(int appWidgetId) {
        return keyPrefix + appWidgetId;
    }

    // Store IDs of AppWidgets that are showing today.
    // AppWidgetManager.getAppWidgetIds() returns all the ids that have been added historically.

    private void addToIds(int id) {

        String idsPrevious = sharedPreferences.getString(idsShowingTodayKey, "");
        StringBuilder idsBuilder = new StringBuilder(idsPrevious);

        if (idsPrevious.length() != 0) {
            idsBuilder.append(IDS_SEPARATOR);
        }
        idsBuilder.append(Integer.toString(id));

        sharedPreferences.edit().putString(idsShowingTodayKey, idsBuilder.toString()).commit();
    }

    private void removeFromIds(int id) {

        String idsPrevious =  sharedPreferences.getString(idsShowingTodayKey, "");

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

        sharedPreferences.edit().putString(idsShowingTodayKey, idsBuilder.toString()).commit();
    }

    public synchronized int[] getIdsShowingToday() {

        String[] idStrings = sharedPreferences.getString(idsShowingTodayKey, "").split(IDS_SEPARATOR);
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

    // Return and set to default value (DATE_INDEX_TODAY) if the date index has not been set.
    public synchronized int get(int appWidgetId) {

        String key = getKey(appWidgetId);
        int dateIndex = sharedPreferences.getInt(key, DATE_INDEX_INVALID);

        if (dateIndex == DATE_INDEX_INVALID) {
            dateIndex = dateIndexToday;
            sharedPreferences.edit().putInt(key, dateIndex).commit();
            addToIds(appWidgetId);
        }

        return dateIndex;
    }

    public synchronized void set(int appWidgetId, int dateIndex) {

        int dateIndexPrevious = sharedPreferences.getInt(getKey(appWidgetId), DATE_INDEX_INVALID);

        sharedPreferences.edit().putInt(getKey(appWidgetId), dateIndex).commit();

        if (dateIndex != dateIndexPrevious) {
            if (dateIndexPrevious == dateIndexToday) {
                removeFromIds(appWidgetId);
            } else if (dateIndex == dateIndexToday) {
                addToIds(appWidgetId);
            }
        }
    }

    public synchronized void remove(int appWidgetId) {

        sharedPreferences.edit().remove(getKey(appWidgetId)).commit();

        removeFromIds(appWidgetId);
    }

    public synchronized void clear() {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (String key : sharedPreferences.getAll().keySet()) {
            if (key.startsWith(keyPrefix)) {
                editor.remove(key);
            }
        }

        editor.remove(idsShowingTodayKey);

        editor.commit();
    }
}
