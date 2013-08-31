package com.myqsc.qscmobile2.xiaoli.uti;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by richard on 13-8-31.
 */
public class XiaoliRange {
    Calendar startTime = null, endTime = null;
    public XiaoliRange(JSONObject jsonObject) throws JSONException, ParseException {
        this(jsonObject.getString("start") ,jsonObject.getString("end"));
    }

    public XiaoliRange(String start, String end) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        startTime = Calendar.getInstance();
        startTime.clear();
        startTime.setTime(simpleDateFormat.parse(start));

        endTime = Calendar.getInstance();
        endTime.clear();
        endTime.setTime(simpleDateFormat.parse(end));
    }
}
