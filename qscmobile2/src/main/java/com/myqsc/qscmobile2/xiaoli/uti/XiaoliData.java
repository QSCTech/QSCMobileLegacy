package com.myqsc.qscmobile2.xiaoli.uti;

import com.myqsc.qscmobile2.uti.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by richard on 13-8-31.
 */
public class XiaoliData {
    XiaoliYearData data[] = null;
    private static final char CHARS[] = {
        '零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十'
    };
    private static final String WEEKS[] = {
        "", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
    };

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
        XiaoliWeek xiaoliWeek = getWeekData(calendar, withReMap);
        if (xiaoliWeek != null)
            return xiaoliWeek.type;
        return -1;
    }

    public XiaoliWeek getWeekData(Calendar calendar, boolean withReMap) {
        if (withReMap)
            calendar = this.doRemap(calendar);
        for(XiaoliYearData xiaoliYearData : data) {
            if (xiaoliYearData.range.inRange(calendar)){
                XiaoliEachYearData xiaoliEachYearData = xiaoliYearData.data;
                if (xiaoliEachYearData.inSession(calendar)) {
                    //判断在学期中（非寒暑假）
                    for(XiaoliWeek xiaoliWeek : xiaoliEachYearData.week){
                        if (xiaoliWeek.inRange(calendar)) {
                            return xiaoliWeek;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取今天是哪个学期
     * @param calendar
     * @param withReMap
     * @return
     */
    public char getTerm(Calendar calendar, boolean withReMap) {
        if (withReMap)
            calendar = this.doRemap(calendar);
        for(XiaoliYearData xiaoliYearData : data) {
            if (xiaoliYearData.range.inRange(calendar)){
                XiaoliEachYearData xiaoliEachYearData = xiaoliYearData.data;
                return xiaoliEachYearData.getTerm(calendar);
            }
        }
        return '无';
    }

    /**
     * 进行日期映射
     * @param calendar
     * @return
     */
    public Calendar doRemap(Calendar calendar) {
        for(XiaoliYearData xiaoliYearData : data) {
            if (xiaoliYearData.range.inRange(calendar)){
                XiaoliEachYearData xiaoliEachYearData = xiaoliYearData.data;
                for(XiaoliReMap xiaoliReMap : xiaoliEachYearData.reMap)
                    calendar = xiaoliReMap.doRemap(calendar);
            }
        }
        return calendar;
    }

    public String getDayString(Calendar calendar, boolean reMap) {
        XiaoliWeek xiaoliWeek = getWeekData(calendar, reMap);
        String string = new String();
        if (xiaoliWeek == null) {
            string = "假期中\n";
        } else {
            string = "第" + CHARS[xiaoliWeek.nth] + "周\n";
        }
        string += WEEKS[calendar.get(Calendar.DAY_OF_WEEK)];
        return string;
    }

    public int getYear(Calendar calendar, boolean withReMap) {
        if (withReMap)
            calendar = this.doRemap(calendar);
        for(XiaoliYearData xiaoliYearData : data) {
            if (xiaoliYearData.range.inRange(calendar)){
                return xiaoliYearData.year;
            }
        }
        return calendar.get(Calendar.YEAR);
    }

    public static String getWeekName(){
        Calendar calendar = Calendar.getInstance();
        return WEEKS[calendar.get(Calendar.DAY_OF_WEEK)];
    }
}
