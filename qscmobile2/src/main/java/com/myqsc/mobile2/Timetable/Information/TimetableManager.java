package com.myqsc.mobile2.Timetable.Information;

import android.content.Context;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


// TODO: Should be made an application-wide singleton. (Lazy initialization?) (Insert to the application?)
// TODO: Synchronization
// TODO: Calendar used should be set to non-lenient.
// TODO: Application context should be passed to here.(?)
public class TimetableManager {

    private static TimetableManager instance = null;

    private Set<TaskProvider> taskProviders = Collections.synchronizedSet(new LinkedHashSet<TaskProvider>());

    private Context context;


    private TimetableManager(Context context) {

        this.context = context;

        // TODO: Move this statement to package Curriculum thus registering there.
        // TODO: Maybe this should be loaded according to permanent data storage.
        taskProviders.add(new CurriculumTaskProvider(context));
    }

    public static void initialize(Context context) {

        // TODO: Remove debug statement.
        if (!context.getClass().equals(context.getApplicationContext().getClass())) {
            throw new RuntimeException("You should use an application Context for singleton.");
        }

        if (instance != null) {
            throw new RuntimeException("This singleton has already been initialized.");
        }

        instance = new TimetableManager(context);
    }

    // TODO: Remove this function because it is only for appwidget debugging.
    public static boolean isInitialized() {
        return instance != null;
    }

    public static TimetableManager getInstance() {

        if (instance == null) {
            throw new RuntimeException("You should initialize the singleton with an application context before using.");
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
