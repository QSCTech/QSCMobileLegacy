package com.myqsc.mobile2.xiaoli.uti;

import com.myqsc.mobile2.uti.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by richard on 13-8-31.
 */
public class XiaoliWeek {
    int nth = 0;
    int type = 0;
    XiaoliRange range = null;

    public XiaoliWeek(JSONObject jsonObject) throws JSONException, ParseException {
        nth = jsonObject.getInt("nth");
        type = jsonObject.getString("type").compareTo("odd") == 0 ?
                Utility.WEEK_ODD : Utility.WEEK_EVEN;
        range = new XiaoliRange(jsonObject);
    }

    public boolean inRange(Calendar calendar) {
        if (range.inRange(calendar))
            return true;
        return false;
    }
}
