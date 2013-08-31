package com.myqsc.qscmobile2.xiaoli.uti;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by richard on 13-8-31.
 * 每年校历全部结构，包括那一年，开始结束日期，以及完整数据
 */
public class XiaoliYearData {
    int year = 0;
    XiaoliRange range = null;
    XiaoliEachYearData data = null;

    public XiaoliYearData(JSONObject jsonObject) throws JSONException, ParseException {
        year = Integer.valueOf(jsonObject.getString("year").substring(0, 4));
        range = new XiaoliRange(jsonObject);
        data = new XiaoliEachYearData(jsonObject.getJSONObject("dtaa"));
    }
}
