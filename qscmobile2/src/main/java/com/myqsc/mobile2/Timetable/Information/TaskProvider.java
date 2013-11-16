package com.myqsc.mobile2.Timetable.Information;

import java.util.Calendar;
import java.util.SortedSet;

public interface TaskProvider {
    public SortedSet<Task> getTasks(Calendar date);
}
