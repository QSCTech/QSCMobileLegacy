package com.myqsc.mobile2.Timetable.Information;

import com.myqsc.mobile2.Utility.TimeUtils;

import java.util.Calendar;

public class Task implements Comparable<Task> {

    private String name;
    private String detail;
    private Calendar startTime;
    private Calendar endTime;


    public Task(String name, String detail, Calendar startTime, Calendar endTime) {
        this.name = name;
        this.detail = detail;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    @Override
    public int compareTo(Task another) {

        int result;

        if ((result = TimeUtils.compare(this.startTime, another.startTime)) == 0) {
            if((result = TimeUtils.compare(this.endTime, another.endTime)) == 0) {
                if((result = this.name.compareTo(another.name)) == 0) {
                    result = this.detail.compareTo(another.detail);
                }
            }
        }

        return result;
    }
}
