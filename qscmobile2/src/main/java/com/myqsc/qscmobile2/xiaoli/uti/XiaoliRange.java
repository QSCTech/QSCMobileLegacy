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
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);
    }

    public boolean inRange(Calendar calendar) {
        if (startTime.compareTo(calendar) <= 0 && endTime.compareTo(calendar) >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return "本周从：" + format.format(startTime.getTime()) + "至：" + format.format(endTime.getTime());
    }
}
