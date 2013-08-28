package com.myqsc.qscmobile2.curriculum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CourseData {
	
	class Class {
		public String week;
		public int weekday;
		public String place;
		public int[] time;
		
		public Class(JSONObject jsonObject) throws JSONException {
			week = jsonObject.getString("week");
			weekday = jsonObject.getInt("weekday");
			place = jsonObject.getString("place");
			JSONArray jsonArray = jsonObject.getJSONArray("class");
			int len = jsonArray.length();
			time = new int[len];
			for (int i = 0; i<len; i++) {
				time[i] = jsonArray.getInt(i);
			}
		}
	}
	
	public String id;
	public String name;
	public String teacher;
	public String semester;
	public String hash;
	public Class[] classes;
	
	public CourseData(JSONObject jsonObject) throws JSONException {
		id = jsonObject.getString("id");
		name = jsonObject.getString("name");
		teacher = jsonObject.getString("teacher");
		semester = jsonObject.getString("semester");
		hash = jsonObject.getString("hash");
		
		JSONArray jsonArray = jsonObject.getJSONArray("class");
		int len = jsonArray.length();
		classes = new Class[len];
		for (int i = 0; i<len; i++) {
			classes[i] = new Class(jsonArray.getJSONObject(i));
		}
	}
}
