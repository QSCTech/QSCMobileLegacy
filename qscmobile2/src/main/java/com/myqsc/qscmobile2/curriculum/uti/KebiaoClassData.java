package com.myqsc.qscmobile2.curriculum.uti;

import com.myqsc.qscmobile2.uti.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoClassData {
    final static String PREFERENCE = "KEBIAO_DATA";

    String name, teacher, place, term;
    int week = 0, year = 0, time = 0;
    int classes[] = null;

    public KebiaoClassData(){
    }

    public static List<KebiaoClassData> parse (JSONArray jsonArray) throws JSONException{
        List<KebiaoClassData> list = new ArrayList<KebiaoClassData>();
        for (int i = 0; i != jsonArray.length(); ++i){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            String rawYear = jsonObject.getString("year");
            int year = Integer.valueOf(rawYear.substring(0, 4));

            List<KebiaoClassData> temp = parseYear(jsonObject.getJSONArray("data"), year);
            for (int j = 0; j != temp.size(); ++j)
                list.add(temp.get(j));
        }
        return list;
    }

    public static List<KebiaoClassData> parseYear(JSONArray jsonArray, int year) throws JSONException{
        List<KebiaoClassData> list = new ArrayList<KebiaoClassData>();

        for (int i = 0; i != jsonArray.length(); ++i){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            String name = jsonObject.getString("name");
            String teacher = jsonObject.getString("teacher");
            String term = jsonObject.getString("semester");

            JSONArray classes = jsonObject.getJSONArray("class");

            for (int j = 0; j != classes.length(); ++j){
                JSONObject object = classes.optJSONObject(j);

                KebiaoClassData data = new KebiaoClassData();
                data.name = name;
                data.teacher = teacher;
                data.place = object.getString("place");
                data.time = object.getInt("weekday");
                data.year = year;
                data.term = term;

                JSONArray classArray = object.getJSONArray("class");
                data.classes = new int[classArray.length()];
                for(int k = 0; k != classArray.length(); ++k)
                    data.classes[k] = classArray.getInt(k);

                String week = object.getString("week");
                if (week.compareTo("both") == 0)
                    data.week = Utility.WEEK_BOTH;
                    if (week.compareTo("odd") == 0)
                        data.week = Utility.WEEK_ODD;
                    else
                        data.week = Utility.WEEK_EVEN;

                list.add(data);
            }
        }
        return list;
    }
}
