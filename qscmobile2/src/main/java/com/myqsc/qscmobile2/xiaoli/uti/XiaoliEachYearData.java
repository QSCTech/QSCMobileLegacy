package com.myqsc.qscmobile2.xiaoli.uti;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;


/**
 * 每年校历结构，指API中的data段数据
 */
public class XiaoliEachYearData {
    XiaoliTerm term[] = null;
    XiaoliWeek week[] = null;
    XiaoliExamWeek examWeek[] = null;
    XiaoliHoliday holiday[] = null;
    XiaoliReMap reMap[] = null;

    /**
     * 构造
     * @param jsonObject API中data段的数据
     * @throws JSONException
     * @throws ParseException
     */
    public XiaoliEachYearData(JSONObject jsonObject) throws JSONException, ParseException {
        //解析寒暑假与学期
        term = new XiaoliTerm[6];
        String termName[] = {
            "chun", "xia", "qiu", "dong", "hanjia", "shujia"
        };
        for(int i = 0; i != termName.length; ++i)
            term[i] = new XiaoliTerm(jsonObject.getJSONObject(termName[i]),
                    termName[i]);

        //解析周次信息
        JSONArray weekJSONArray = jsonObject.getJSONArray("weeks");
        week = new XiaoliWeek[weekJSONArray.length()];
        for (int i = 0; i != weekJSONArray.length(); ++i) {
            week[i] = new XiaoliWeek(weekJSONArray.optJSONObject(i));
        }

        //解析考试数据
        JSONArray examJSONArray = jsonObject.getJSONArray("exams");
        examWeek = new XiaoliExamWeek[examJSONArray.length()];
        for(int i = 0; i != examJSONArray.length(); ++i){
            examWeek[i] = new XiaoliExamWeek(examJSONArray.optJSONObject(i));
        }

        //解析假期数据
        JSONArray holidayJSONArray = jsonObject.getJSONArray("holidays");
        holiday = new XiaoliHoliday[holidayJSONArray.length()];
        for (int i = 0; i != holidayJSONArray.length(); ++i) {
            holiday[i] = new XiaoliHoliday(holidayJSONArray.optJSONObject(i));
        }

        //解析日期映射数据
        JSONArray remapJSONArray = jsonObject.getJSONArray("remap");
        reMap = new XiaoliReMap[remapJSONArray.length()];
        for (int i = 0; i != remapJSONArray.length(); ++i) {
            reMap[i] = new XiaoliReMap(remapJSONArray.optJSONObject(i));
        }
    }

    public boolean inSession(Calendar calendar) {
        for (XiaoliTerm xiaoliTerm : term) {
            if (xiaoliTerm.inRange(calendar)) {
                if (xiaoliTerm.termName.compareTo("hanjia") != 0
                        && xiaoliTerm.termName.compareTo("shujia") != 0) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
