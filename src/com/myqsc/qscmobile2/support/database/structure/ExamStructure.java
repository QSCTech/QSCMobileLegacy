package com.myqsc.qscmobile2.support.database.structure;

import org.json.JSONException;
import org.json.JSONObject;


import android.database.Cursor;

public class ExamStructure {
	public final static String PREFERENCE = "ExamData";
	public String course_num, course_name, credit, stu_name, term, time, position, seat, is_rebuild;
	
	public ExamStructure(){
	};
	
	public ExamStructure(JSONObject jsonObject) throws JSONException {
		course_num = jsonObject.getString("选课课号").trim();
		course_name = jsonObject.getString("课程名称").trim();
		credit = jsonObject.getString("学分").trim();
		is_rebuild = jsonObject.getString("重修标记").trim();
		stu_name = jsonObject.getString("姓名").trim();
		term = jsonObject.getString("学期").trim();
		time = jsonObject.getString("考试时间").trim();
		position = jsonObject.getString("考试地点").trim();
		seat = jsonObject.getString("考试座位号").trim();
	}
	
	public JSONObject toJsonObject() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("选课课号", course_num);
		jsonObject.put("课程名称", course_name);
		jsonObject.put("学分", credit);
		jsonObject.put("重修标记", is_rebuild);
		jsonObject.put("姓名", stu_name);
		jsonObject.put("学期", term);
		jsonObject.put("考试时间", time);
		jsonObject.put("考试地点", position);
		jsonObject.put("考试座位号", seat);
		return jsonObject;
	}
}
