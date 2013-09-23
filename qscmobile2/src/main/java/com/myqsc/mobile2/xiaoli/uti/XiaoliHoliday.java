package com.myqsc.mobile2.xiaoli.uti;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by richard on 13-8-31.
 */
public class XiaoliHoliday {
    String name = null;
    XiaoliRange range = null;
    public XiaoliHoliday(JSONObject jsonObject) throws JSONException, ParseException {
        name = jsonObject.getString("name");
        range = new XiaoliRange(jsonObject);
    }

    public boolean inRange(Calendar calendar) {
        return range.inRange(calendar);
    }
}
