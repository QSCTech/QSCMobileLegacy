package com.myqsc.qscmobile2.xiaoli.uti;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.Calendar;

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

    /**
     * 判断这一天是单周还是双周
     * @param calendar 这一天
     * @param withReMap 是否使用remap数据
     * @return int 特殊数字，表示单双周
     */
    public int checkParity(Calendar calendar, boolean withReMap){
        for(XiaoliYearData xiaoliYearData : data) {
            if (xiaoliYearData.range.inRange(calendar)){
                XiaoliEachYearData xiaoliEachYearData = xiaoliYearData.data;
                if (withReMap) {
                    for(XiaoliReMap xiaoliReMap : xiaoliEachYearData.reMap)
                        calendar = xiaoliReMap.doRemap(calendar);
                }

                for(XiaoliWeek xiaoliWeek : xiaoliEachYearData.week){
                    if (xiaoliWeek.inRange(calendar)) {
                        return xiaoliWeek.type;
                    }
                }
            }
        }
        return -1;
    }
}
