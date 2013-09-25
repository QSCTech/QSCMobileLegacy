package com.myqsc.mobile2.chengji.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by richard on 13-9-25.
 */
public class GradeClassStructure {
    public String courseID, name, grade, credit, gradePoint, markUpExam;

    public GradeClassStructure(JSONObject jsonObject) throws JSONException {
        courseID    = jsonObject.getString("选课课号");
        name        = jsonObject.getString("课程名称");
        grade       = jsonObject.getString("成绩");
        credit      = jsonObject.getString("学分");
        gradePoint  = jsonObject.getString("绩点");
        markUpExam  = jsonObject.getString("补考成绩");
    }
}
