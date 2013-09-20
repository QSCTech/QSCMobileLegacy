package com.myqsc.mobile2.xiaoli.uti;

import com.myqsc.mobile2.uti.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by richard on 13-8-31.
 */
public class XiaoliTerm {
    String termName = null;
    char name = 0x0;
    XiaoliRange range = null;
    public XiaoliTerm(JSONObject jsonObject, String termName, char name) throws JSONException, ParseException {
        range = new XiaoliRange(jsonObject);
        this.termName = termName;
        this.name = name;
    }

    public boolean inRange(Calendar calendar) {
        return range.inRange(calendar);
    }
}
