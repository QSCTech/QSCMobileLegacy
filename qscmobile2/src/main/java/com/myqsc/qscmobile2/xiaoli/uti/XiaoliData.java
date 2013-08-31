package com.myqsc.qscmobile2.xiaoli.uti;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by richard on 13-8-31.
 */
public class XiaoliData {
    XiaoliYearData data[] = null;

    public XiaoliData(JSONArray jsonArray) throws JSONException, ParseException {
        data = new XiaoliYearData[jsonArray.length()];
        for(int i = 0; i != jsonArray.length(); ++i)
            data[i] = new XiaoliYearData(jsonArray.optJSONObject(i));
    }
}
