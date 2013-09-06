package com.myqsc.qscmobile2.exam.uti;

import org.json.JSONException;
import org.json.JSONObject;


import android.database.Cursor;

import com.myqsc.qscmobile2.uti.LogHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExamStructure {
	public final static String PREFERENCE = "ExamData";
	public String course_num, course_name, credit, stu_name, term, time, position, seat, is_rebuild;
	
	public ExamStructure(){
	};

    public Calendar getStartTime()  {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.set(Calendar.YEAR, Integer.parseInt(time.substring(0, 4)));
            calendar.set(Calendar.MONTH, Integer.parseInt(time.substring(5, 7)) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(time.substring(8, 10)));
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(12, 14)));
            calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(15, 17)));
            calendar.set(Calendar.SECOND, 0);
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }
        return calendar;
    }

    public boolean isToday(Calendar calendar) {
        Calendar timeCalendar = getStartTime();
        if (timeCalendar != null && timeCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)){
            LogHelper.d(course_name);
            return true;
        }
        return false;
    }
	
	public ExamStructure(JSONObject jsonObject) throws JSONException {
		course_num  = jsonObject.getString("选课课号").trim();
		course_name = jsonObject.getString("课程名称").trim();
		credit      = jsonObject.getString("学分").trim();
		is_rebuild  = jsonObject.getString("重修标记").trim();
		stu_name    = jsonObject.getString("姓名").trim();
		term        = jsonObject.getString("学期").trim();
		time        = jsonObject.getString("考试时间").trim();
		position    = jsonObject.getString("考试地点").trim();
		seat        = jsonObject.getString("考试座位号").trim();
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
