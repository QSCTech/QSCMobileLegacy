package com.myqsc.mobile2.xiaoli.uti;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by richard on 13-8-31.
 */
public class XiaoliExamWeek {
    String name = null;
    XiaoliRange range = null;
    public XiaoliExamWeek(JSONObject jsonObject) throws JSONException, ParseException {
        name = jsonObject.getString("name");
        range = new XiaoliRange(jsonObject);
    }
}
