package com.myqsc.mobile2.Timetable.Information;

import android.content.Context;
import android.support.v4.util.LruCache;

import com.myqsc.mobile2.Utility.TimeUtils;
import com.myqsc.mobile2.uti.LogHelper;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

// TODO: Synchronization confirmation.
// TODO: Invalidate the cache by cache.evictAll() when changes are made.
public class TimetableManager {

    private static final int CACHE_SIZE = 3;

    private static final Object instanceLock = new Object();

    private static TimetableManager instance = null;

    private Context context;

    private Set<TaskProvider> taskProviders = Collections.synchronizedSet(new LinkedHashSet<TaskProvider>());

    // NOTE: java.util.WeakHashMap holds weak references of keys, not values.
    private final LruCache<Calendar, SortedSet<Task>> cache = new LruCache<Calendar, SortedSet<Task>>(CACHE_SIZE);


    private TimetableManager(Context context) {

        this.context = context;

        // TODO: Move this statement to package Curriculum & Examination thus registering there.
        // TODO: Maybe this should be loaded according to permanent data storage.
        taskProviders.add(new CurriculumTaskProvider(context));
        taskProviders.add(new ExaminationTaskProvider(context));

        // TODO: Remove debug statement.
        //taskProviders.add(new DummyTaskProvider());
    }

    public static TimetableManager getInstance(Context context) {

        synchronized (instanceLock) {

            if (instance == null) {
                instance = new TimetableManager(context.getApplicationContext());
            }

            return instance;
        }
    }

    public SortedSet<Task> getTimetable(Calendar date) {

        // REMOVEME: Debug statement.
        Calendar dateCopy = (Calendar) date.clone();
        TimeUtils.clearTime(dateCopy);
        if (!date.equals(dateCopy)) {
            throw new RuntimeException("Should use a date with time cleared here.");
        }

        SortedSet<Task> timetable;

        synchronized (cache) {

            timetable = cache.get(date);

            if (timetable == null) {

                timetable = Collections.synchronizedSortedSet(new TreeSet<Task>());

                for (TaskProvider taskProvider : taskProviders) {
                    timetable.addAll(taskProvider.getTasks(date));
                }

                cache.put(date, timetable);
            }

            LogHelper.i(cache.toString());
        }

        return timetable;
    }
}
