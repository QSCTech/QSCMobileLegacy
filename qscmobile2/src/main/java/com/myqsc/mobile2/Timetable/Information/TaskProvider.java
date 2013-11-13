package com.myqsc.mobile2.Timetable.Information;

import java.util.Calendar;
import java.util.SortedSet;

public interface TaskProvider {
    SortedSet<Task> getTasks(Calendar startTime, Calendar endTime);
}
