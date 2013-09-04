package com.myqsc.qscmobile2.curriculum.uti;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;

import com.myqsc.qscmobile2.network.DataUpdater;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.login.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.uti.Utility;
import com.myqsc.qscmobile2.xiaoli.uti.XiaoliHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoDataHelper {
    HandleAsyncTaskMessage handleAsyncTaskMessage = null;
    Context mContext = null;
    List<KebiaoClassData> todayKebiaolist = null;

    public KebiaoDataHelper(Context context){
        this.mContext = context;
    }

    public void setHandleAsyncTaskMessage(HandleAsyncTaskMessage message){
        handleAsyncTaskMessage = message;
    }

    public void clear(){
        mContext
                .getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .remove(DataUpdater.JW_KEBIAO)
                .commit();
    }

    public void set(String result){
        clear();
        mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .putString(DataUpdater.JW_KEBIAO, result)
                .commit();
    }

    public void getDay (final Calendar calendar){
        if (todayKebiaolist != null) {
            if (handleAsyncTaskMessage != null) {
                Message message = new Message();
                message.what = 1;
                message.obj = todayKebiaolist;
                handleAsyncTaskMessage.onHandleMessage(message);
            }
            return ;
        }
        String result = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(DataUpdater.JW_KEBIAO, null);
        LogHelper.d(result);

        try {
            getTodayKebiao(KebiaoClassData.parse(new JSONArray(result)), calendar);
        } catch (JSONException e) {
            e.printStackTrace();
            clear();
            getDay(calendar);
        }
    }

    private void getTodayKebiao(List<KebiaoClassData> allKebiaoList, Calendar calendar) {
        todayKebiaolist = new ArrayList<KebiaoClassData>();
        XiaoliHelper xiaoliHelper = new XiaoliHelper(mContext);
        calendar = xiaoliHelper.doRemap(calendar);
        int week = xiaoliHelper.checkParity(calendar, false);
        char term = xiaoliHelper.getTerm(calendar, false);
        int year = xiaoliHelper.getYear(calendar, false);

        for (KebiaoClassData data : allKebiaoList) {
//            LogHelper.d(data.toString());
            if (data.year != year) {
//                LogHelper.d("different year:" + data);
                continue;
            }
            if (data.term.indexOf(term) == -1) {
//                LogHelper.d("different Term:" + data);
                continue;
            }

            if (data.week != Utility.WEEK_BOTH && data.week != week) {
//                LogHelper.d("different week:" + data + "origin week:" + data.week);
                continue;
            }

            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            if (weekday == 1)
                weekday = 8;
            --weekday;
            if (data.time != weekday)
                continue;

            LogHelper.d("today: " + data);
            todayKebiaolist.add(data);
        }

        Collections.sort(todayKebiaolist, new Comparator<KebiaoClassData>() {
            @Override
            public int compare(KebiaoClassData kebiaoClassData, KebiaoClassData kebiaoClassData2) {
                return kebiaoClassData.classes[0] - kebiaoClassData2.classes[0];
            }
        });

        if (handleAsyncTaskMessage != null) {
            Message message = new Message();
            message.what = 1;
            message.obj = todayKebiaolist;
            handleAsyncTaskMessage.onHandleMessage(message);
        }
    }
}
