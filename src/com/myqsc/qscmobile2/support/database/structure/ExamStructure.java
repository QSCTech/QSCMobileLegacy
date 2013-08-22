package com.myqsc.qscmobile2.support.database.structure;

import org.json.JSONException;
import org.json.JSONObject;

public class ExamStructure {
	public String course_num, course_name, credit, stu_name, term, time, position, seat, is_rebuild;
	
	public ExamStructure(){
		
	};
	
	public ExamStructure(JSONObject jsonObject) throws JSONException {
		course_num = jsonObject.getString("选课课号");
		course_name = jsonObject.getString("课程名称");
		credit = jsonObject.getString("学分");
		is_rebuild = jsonObject.getString("重修标记");
		stu_name = jsonObject.getString("姓名");
		term = jsonObject.getString("学期");
		time = jsonObject.getString("考试时间");
		position = jsonObject.getString("考试地点");
		seat = jsonObject.getString("考试座位号");
	}
}
