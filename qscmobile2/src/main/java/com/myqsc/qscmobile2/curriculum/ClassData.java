package com.myqsc.qscmobile2.curriculum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClassData implements Comparable<ClassData> {
	
	public String name;
	public String teacher;
	public String time;
	public String place;

    public final static String PREFERENCE = "CLASSDATA";
	
	ClassData(String name,String teacher,String time,String place) {
		this.name = name;
		this.teacher = teacher; 
		this.time = time;
		this.place =place;
	}



	@Override
	public int compareTo(ClassData another) {
		// FIXME 改为标准的比较方式
		if (!time.equals(another.time)) {
			return time.compareTo(another.time);
		}
		return 0;
	}

}