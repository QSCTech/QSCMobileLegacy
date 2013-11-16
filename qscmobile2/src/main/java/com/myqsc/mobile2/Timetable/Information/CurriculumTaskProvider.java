package com.myqsc.mobile2.Timetable.Information;

import android.content.Context;

import com.myqsc.mobile2.Utility.TimeUtils;
import com.myqsc.mobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.mobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.mobile2.curriculum.uti.KebiaoUtility;

import java.util.Calendar;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

// TODO: Move this class to package Curriculum

public class CurriculumTaskProvider implements TaskProvider {

    private Context context;

    private KebiaoDataHelper kebiaoDataHelper;


    public CurriculumTaskProvider(Context context) {

        // TODO: Remove debug statement.
        if (!context.getClass().equals(context.getApplicationContext().getClass())) {
            throw new RuntimeException("You should use an application Context for consistency although it doesn't matter at present :(");
        }

        this.context = context;
        kebiaoDataHelper = new KebiaoDataHelper(context);
    }

    // TODO: Refactor this!
    private void setTimeFromString(Calendar time, String string) {
        String[] strings = string.split(":");
        TimeUtils.setTime(time, Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
    }

    public SortedSet<Task> getTasks(Calendar date) {

        SortedSet<Task> tasks = Collections.synchronizedSortedSet(new TreeSet<Task>());

        for (KebiaoClassData kebiaoClassData : kebiaoDataHelper.getDay(date)) {

            Calendar startTime = (Calendar) date.clone();
            Calendar endTime = (Calendar) date.clone();
            setTimeFromString(startTime, KebiaoUtility.classFrom[kebiaoClassData.classes[0]]);
            setTimeFromString(endTime, KebiaoUtility.classTo[kebiaoClassData.classes[kebiaoClassData.classes.length - 1]]);

            tasks.add(new Task(kebiaoClassData.name, kebiaoClassData.place, startTime, endTime));
        }
        return tasks;
    }
}
