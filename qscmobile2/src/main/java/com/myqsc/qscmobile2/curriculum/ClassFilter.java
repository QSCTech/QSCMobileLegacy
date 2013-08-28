package com.myqsc.qscmobile2.curriculum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.AsyncTask;

public class ClassFilter extends AsyncTask<List<CourseData>, Void, List<ClassData>> {

	public int dayOfWeek;
	
	public ClassFilter(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	
	@Override
	protected List<ClassData> doInBackground(
			final List<CourseData>... courses) {
		List<ClassData> classes = new ArrayList<ClassData>();
		for (CourseData course: courses[0]) {
			for (CourseData.Class cls: course.classes) {
				if (cls.weekday!=dayOfWeek) continue;
				//FIXME cls.time格式
				classes.add(new ClassData(course.name,
						                  course.teacher,
						                  cls.time.toString(),
						                  cls.place));
			}
		}
		Collections.sort(classes);
		return classes;
	}
	
}
