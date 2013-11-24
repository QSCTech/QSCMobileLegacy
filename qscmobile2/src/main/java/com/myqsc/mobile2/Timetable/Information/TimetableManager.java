package com.myqsc.mobile2.Timetable.Information;

import android.content.Context;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

// TODO: Synchronization confirmation.
public class TimetableManager {

    private static TimetableManager instance = null;

    private Set<TaskProvider> taskProviders = Collections.synchronizedSet(new LinkedHashSet<TaskProvider>());

    private Context context;


    private TimetableManager(Context context) {

        this.context = context;

        // TODO: Move this statement to package Curriculum thus registering there.
        // TODO: Maybe this should be loaded according to permanent data storage.
        taskProviders.add(new CurriculumTaskProvider(context));

        // TODO: Remove debug statement.
        //taskProviders.add(new DummyTaskProvider());
    }

    public static TimetableManager getInstance(Context context) {

        synchronized (TimetableManager.class) {
            if (instance == null) {
                instance = new TimetableManager(context.getApplicationContext());
            }
        }

        return instance;
    }

    public SortedSet<Task> getTimetable(Calendar date) {

        SortedSet<Task> timetable = Collections.synchronizedSortedSet(new TreeSet<Task>());

        for (TaskProvider taskProvider : taskProviders) {
            timetable.addAll(taskProvider.getTasks(date));
        }

        return timetable;
    }
}
