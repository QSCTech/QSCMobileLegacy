package com.myqsc.mobile2.Timetable.Information;

import java.util.Calendar;

public class Task {

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

}
