package com.myqsc.mobile2.Timetable.Information;

import android.content.Context;

import com.myqsc.mobile2.Utility.TimeUtils;
import com.myqsc.mobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.mobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.mobile2.curriculum.uti.KebiaoUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;


// TODO: Should be made an application-wide singleton.
// TODO: Synchronization
public class TaskManager {

    private static SortedSet<TaskProvider> taskProviders = Collections.synchronizedSortedSet(new TreeSet<TaskProvider>());

    private Context context;

    private KebiaoDataHelper kebiaoDataHelper;

    private TaskManager(Context context) {
        this.context = context;
        kebiaoDataHelper = new KebiaoDataHelper(context);
    }

    public SortedSet<Task> getTimetableByDate(Calendar date) {
        SortedSet<Task> timetable = Collections.synchronizedSortedSet(new TreeSet<Task>());

        Calendar startTime = (Calendar) date.clone();
        TimeUtils.clearTime(startTime);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.DAY_OF_YEAR, 1);

        for (TaskProvider taskProvider : taskProviders) {
            timetable.addAll(taskProvider.getTasks(startTime, endTime));
        }

        // TODO: Refactor!
        for (KebiaoClassData kebiaoClassData : kebiaoDataHelper.getDay(date)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:ss");
            simpleDateFormat.setLenient(false);
            Calendar classStartTime = (Calendar) date.clone();
            Calendar classEndTime = (Calendar) date.clone();
            try {
                classStartTime.setTime(simpleDateFormat.parse(KebiaoUtility.classFrom[kebiaoClassData.classes[0]]));
                classEndTime.setTime(simpleDateFormat.parse(KebiaoUtility.classFrom[kebiaoClassData.classes[kebiaoClassData.classes.length - 1]]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            timetable.add(new Task(kebiaoClassData.name, kebiaoClassData.place, startTime, endTime));
        }

        return timetable;
    }
}
