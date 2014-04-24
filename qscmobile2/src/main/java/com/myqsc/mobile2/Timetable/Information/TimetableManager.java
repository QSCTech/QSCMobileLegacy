package com.myqsc.mobile2.Timetable.Information;

import android.content.Context;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

// TODO: Synchronization confirmation.
// TODO: Invalidate the cache by cache.evictAll() when changes are made.
public class TimetableManager {

    private static final Object instanceLock = new Object();

    private static TimetableManager instance = null;

    private Context context;

    private Set<TaskProvider> taskProviders = Collections.synchronizedSet(new LinkedHashSet<TaskProvider>());


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

        SortedSet<Task> timetable = Collections.synchronizedSortedSet(new TreeSet<Task>());

        for (TaskProvider taskProvider : taskProviders) {
            timetable.addAll(taskProvider.getTasks(date));
        }

        return timetable;
    }
}
