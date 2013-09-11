package com.myqsc.qscmobile2.curriculum.uti;

import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.Utility;
import com.myqsc.qscmobile2.xiaoli.uti.XiaoliData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoClassData {
    public String name, teacher, place, term;
    public int week = 0, year = 0, time = 0;
    //week 单双周，time 周几
    int classes[] = null;

    public KebiaoClassData() {
    }




    public static List<KebiaoClassData> parseYear(JSONArray jsonArray, int year) throws JSONException{
        List<KebiaoClassData> list = new ArrayList<KebiaoClassData>();

        for (int i = 0; i != jsonArray.length(); ++i){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            String name = jsonObject.getString("name");
            String teacher = jsonObject.getString("teacher");


            JSONArray classes = jsonObject.getJSONArray("class");

            for (int j = 0; j != classes.length(); ++j){
                JSONObject object = classes.optJSONObject(j);

                KebiaoClassData data = new KebiaoClassData();
                data.name = name;
                data.teacher = teacher;
                data.place = object.getString("place");
                data.time = object.getInt("weekday");
                data.year = year;
                data.term = object.getString("semester");

                JSONArray classArray = object.getJSONArray("class");
                data.classes = new int[classArray.length()];
                for(int k = 0; k != classArray.length(); ++k)
                    data.classes[k] = classArray.getInt(k);

                String week = object.getString("week");
                if (week.compareTo("both") == 0) {
                    data.week = Utility.WEEK_BOTH;
                } else {
                    if (week.compareTo("odd") == 0)
                        data.week = Utility.WEEK_ODD;
                    else
                        data.week = Utility.WEEK_EVEN;
                }
                list.add(data);
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean inRange(int course){
        for (int i : classes)
            if (i == course)
                return true;
        return false;
    }

    public String classString(){
        String string = XiaoliData.getWeekName() + " ";
        for (int i : classes)
            string += i + "/";
        return string.substring(0, string.length() - 1);
    }
}
