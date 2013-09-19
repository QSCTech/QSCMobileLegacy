package com.myqsc.qscmobile2.curriculum.uti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;

import com.myqsc.qscmobile2.network.DataUpdater;
import com.myqsc.qscmobile2.network.UpdateHelper;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.login.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.uti.Utility;
import com.myqsc.qscmobile2.xiaoli.uti.XiaoliHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoDataHelper {
    Context mContext = null;
    List<KebiaoClassData> kebiaoList = null;
    List<KebiaoClassData> todayKebiaolist = null;
    int whichDay = 0;

    public KebiaoDataHelper(Context context) {
        this.mContext = context;
        kebiaoList = parse(mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(DataUpdater.JW_KEBIAO, null));
    }

    public List<KebiaoClassData> parse(String string) {
        List<KebiaoClassData> list = new ArrayList<KebiaoClassData>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            for (int i = 0; i != jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String rawYear = jsonObject.getString("year");
                int year = Integer.valueOf(rawYear.substring(0, 4));

                List<KebiaoClassData> temp = KebiaoClassData.parseYear(jsonObject.getJSONArray("data"), year);
                for (int j = 0; j != temp.size(); ++j)
                    list.add(temp.get(j));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<KebiaoClassData> getDay(final Calendar calendar) {
        if (whichDay != calendar.get(Calendar.DATE) || todayKebiaolist == null){
            todayKebiaolist = getTodayKebiao(kebiaoList, calendar);
            whichDay = calendar.get(Calendar.DATE);
        }
        return todayKebiaolist;
    }

    XiaoliHelper xiaoliHelper = null;

    private List<KebiaoClassData> getTodayKebiao(List<KebiaoClassData> allKebiaoList, Calendar calendar) {
        List<KebiaoClassData> list = new ArrayList<KebiaoClassData>();
        if (xiaoliHelper == null)
            xiaoliHelper = new XiaoliHelper(mContext);

        calendar = xiaoliHelper.doRemap(calendar);
        if (xiaoliHelper.checkHoliday(calendar) != null)
            return list;
        int week = xiaoliHelper.checkParity(calendar, false);
        char term = xiaoliHelper.getTerm(calendar, false);
        int year = xiaoliHelper.getYear(calendar, false);

        for (KebiaoClassData data : allKebiaoList) {
            if (data.year != year) {
                continue;
            }
            if (data.term.indexOf(term) == -1) {
                continue;
            }

            if (data.week != Utility.WEEK_BOTH && data.week != week) {
                continue;
            }

            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            if (weekday == 1)
                weekday = 8;
            --weekday;
            if (data.time != weekday)
                continue;

            LogHelper.d("today: " + data);
            list.add(data);
        }

        Collections.sort(list, new Comparator<KebiaoClassData>() {
            @Override
            public int compare(KebiaoClassData kebiaoClassData, KebiaoClassData kebiaoClassData2) {
                return kebiaoClassData.classes[0] - kebiaoClassData2.classes[0];
            }
        });
        return list;
    }
}
