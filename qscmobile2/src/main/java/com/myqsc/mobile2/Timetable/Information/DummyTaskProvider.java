package com.myqsc.mobile2.Timetable.Information;

import com.myqsc.mobile2.Utility.TimeUtils;
import com.myqsc.mobile2.uti.LogHelper;

import java.util.Calendar;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class DummyTaskProvider implements TaskProvider {

    private static Calendar base;

    public DummyTaskProvider() {
        base = TimeUtils.getNow();
        base.add(Calendar.SECOND, 30);
        LogHelper.i("Countdown left: " + (System.currentTimeMillis() - base.getTimeInMillis() / 1000));
    }

    @Override
    public SortedSet<Task> getTasks(Calendar date) {

        SortedSet<Task> tasks = Collections.synchronizedSortedSet(new TreeSet<Task>());

        if (TimeUtils.isToday(TimeUtils.getNow())) {

            for (int i = 0; i != 4; ++i) {

                // No overlap now.

                Calendar startTime = (Calendar) base.clone();
    //            startTime.add(Calendar.SECOND, 10 * (2 * i - 1));
                startTime.add(Calendar.SECOND, 10 * (15 - 2 * i - 1));
                Calendar endTime = (Calendar) base.clone();
    //            endTime.add(Calendar.SECOND, 10 * 2 * i);
                endTime.add(Calendar.SECOND, 10 * (15 - 2 * i));

                tasks.add(new Task("懒得取名字 " + i, "没有详细信息嘛~", startTime, endTime));
            }
        }

        return tasks;
    }
}
