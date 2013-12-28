package com.myqsc.mobile2.Timetable.Information;

import android.content.Context;

import com.myqsc.mobile2.Utility.TimeUtils;
import com.myqsc.mobile2.exam.uti.ExamDataHelper;
import com.myqsc.mobile2.exam.uti.ExamStructure;
import com.myqsc.mobile2.uti.LogHelper;

import java.util.Calendar;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

// TODO: Move this class to package Curriculum

public class ExaminationTaskProvider implements TaskProvider {

    private Context context;

    private ExamDataHelper examDataHelper;


    public ExaminationTaskProvider(Context context) {
        this.context = context;
        examDataHelper = new ExamDataHelper(context);
    }

    // TODO: Refactor this!
    @Override
    public SortedSet<Task> getTasks(Calendar date) {

        SortedSet<Task> tasks = Collections.synchronizedSortedSet(new TreeSet<Task>());

        for (ExamStructure examStructure : examDataHelper.getTodayExamList(date)) {

            // TODO: Remove debug statement.
            // Time Should have been specified.
            if (examStructure.time.length() < 24) {
                LogHelper.e("Time string too short for " + examStructure.course_name);
                continue;
            }

            Calendar startTime = (Calendar) date.clone();
            Calendar endTime = (Calendar) date.clone();
            try {
                TimeUtils.setTime(startTime,
                        Integer.parseInt(examStructure.time.substring(12, 14)),
                        Integer.parseInt(examStructure.time.substring(15, 17)));
                TimeUtils.setTime(endTime,
                        Integer.parseInt(examStructure.time.substring(18, 20)),
                        Integer.parseInt(examStructure.time.substring(21, 23)));
            } catch (NumberFormatException e) {
                LogHelper.e("Failed to parse time for " + examStructure.course_name);
                e.printStackTrace();
                continue;
            }

            tasks.add(new Task(examStructure.course_name, examStructure.position + examStructure.seat, startTime, endTime));
        }

        return tasks;
    }
}
