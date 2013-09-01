package com.myqsc.qscmobile2.curriculum.uti;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;

import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.uti.Utility;
import com.myqsc.qscmobile2.xiaoli.uti.XiaoliHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoDataHelper {
    HandleAsyncTaskMessage handleAsyncTaskMessage = null;
    Context mContext = null;

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
                .remove(KebiaoClassData.PREFERENCE)
                .commit();
    }

    public void set(String result){
        clear();
        mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .putString(KebiaoClassData.PREFERENCE, result)
                .commit();
    }

    public void getDay (final Calendar calendar){
        String result = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(KebiaoClassData.PREFERENCE, null);
        UserIDStructure userIDStructure = new PersonalDataHelper(mContext).getCurrentUser();
        if (result == null) {
            final UpdateKebiaoAsyncTask task = new UpdateKebiaoAsyncTask(mContext, userIDStructure) {
                @Override
                public void onHandleMessage(Message message) {
                    if (message.what != 0)
                        getDay(calendar);
                    else
                        if (handleAsyncTaskMessage != null)
                            handleAsyncTaskMessage.onHandleMessage(message);
                }
            };
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                task.execute();
        } else {
            try {
                getTodayKebiao(KebiaoClassData.parse(new JSONArray(result)), calendar);
            } catch (JSONException e) {
                e.printStackTrace();
                clear();
                getDay(calendar);
            }
        }
    }

    private List<KebiaoClassData> getTodayKebiao(List<KebiaoClassData> allKebiaoList, Calendar calendar) {
        List<KebiaoClassData> list = new ArrayList<KebiaoClassData>();
        XiaoliHelper xiaoliHelper = new XiaoliHelper(mContext);
        int week = xiaoliHelper.checkParity(calendar, true);
        char term = xiaoliHelper.getTerm(calendar, true);
        int year = xiaoliHelper.getYear(calendar, true);

        for (KebiaoClassData data : allKebiaoList) {
            if (data.year != year)
                continue;
            if (data.term.indexOf(term) == -1)
                continue;
            if (data.week != Utility.WEEK_BOTH && data.week != week)
                continue;
            list.add(data);
        }
        return list;
    }
}
