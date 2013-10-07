package com.myqsc.mobile2.Grade.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by richard on 13-9-25.
 */
public class GradeAverageStructure {
    public String credit, grade, time;

    public GradeAverageStructure(JSONObject jsonObject) throws JSONException {
        credit      = jsonObject.getString("总学分");
        grade       = jsonObject.getString("均绩");
        time        = jsonObject.getString("时间");
    }
}
