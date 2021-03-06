package com.myqsc.mobile2.xiaoli.uti;

import android.content.Context;

import com.myqsc.mobile2.network.DataUpdater;
import com.myqsc.mobile2.uti.Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by richard on 13-8-31.
 */
public class XiaoliHelper {
    Context mContext = null;
    XiaoliData data = null;

    public XiaoliHelper(Context context) {
        this.mContext = context;
        try {
            data = parse();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data == null)
            data = new XiaoliData();
    }

    private XiaoliData parse() throws JSONException, ParseException, IOException {
        String result = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(DataUpdater.COMMON_XIAOLI, null);
        data = new XiaoliData(new JSONArray(result));
        return data;    
    }

    /**
     * 判断这一天属于单周还是双周
     * @param calendar 要测试的日期
     * @param withRemap flase 表示不使用remap的值，true表示使用remap的值
     */
    public int checkParity(Calendar calendar, boolean withRemap){
        return data.checkParity(calendar, withRemap);
    }

    /**
     * 判断今天是哪个学期
     * @param calendar
     * @param withReMap
     * @return
     */
    public char getTerm(Calendar calendar, boolean withReMap) {
        return data.getTerm(calendar, withReMap);
    }

    /**
     * 获取显示在校历卡片上的字符串
     * @param calendar
     * @param withReMap
     * @return
     */
    public String getDayString(Calendar calendar, boolean withReMap) {
        return data.getDayString(calendar, withReMap);
    }

    /**
     * 获取今天是哪个学年
     * @param calendar
     * @param withReMap
     * @return
     */
    public int getYear(Calendar calendar, boolean withReMap) {
        return data.getYear(calendar, withReMap);
    }

    /**
     * 进行日期映射
     * @param calendar
     * @return
     */
    public Calendar doRemap(Calendar calendar) {
        return data.doRemap(calendar);
    }

    /**
     * 获取今天是不是假期
     * @param calendar
     * @return
     */
    public XiaoliHoliday checkHoliday(Calendar calendar) {
        return data.getHoliday(calendar);
    }

    public XiaoliExamWeek checkExamWeek (Calendar calendar) {
        return data.getExamWeek(calendar);
    }
}
