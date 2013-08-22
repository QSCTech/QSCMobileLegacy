package com.myqsc.qscmobile2.support.database.structure;

import org.json.JSONException;
import org.json.JSONObject;

public class ExamStructure {
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
}
