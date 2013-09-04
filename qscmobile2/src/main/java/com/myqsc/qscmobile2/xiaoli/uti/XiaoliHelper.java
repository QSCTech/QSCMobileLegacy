package com.myqsc.qscmobile2.xiaoli.uti;

import android.content.Context;
import android.os.Message;

import com.myqsc.qscmobile2.network.DataUpdater;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
            parse();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear(String result) {
        mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .remove(DataUpdater.COMMON_XIAOLI)
                .commit();
    }

    public void set(String result) {
        mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .putString(DataUpdater.COMMON_XIAOLI, result)
                .commit();
    }

    /**
     * 更新校历数据，完成后回调 handleAsyncTaskMessage，可为 null
     * @param handleAsyncTaskMessage
     */
    public void update(HandleAsyncTaskMessage handleAsyncTaskMessage){
        Message message = new Message();
        message.what = 0;
        try {
            StringBuffer buffer = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(mContext.getAssets().open("xiaoli.json"));
            char b[] = new char[1024];
            while (reader.read(b) != -1) {
                buffer.append(b);
            }
            String result = String.valueOf(buffer);
            mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                    .edit()
                    .putString(DataUpdater.COMMON_XIAOLI, result)
                    .commit();
            parse();

            message.what = 1;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (handleAsyncTaskMessage != null) {
            handleAsyncTaskMessage.onHandleMessage(message);
        }
    }

    private XiaoliData parse() throws JSONException, ParseException, IOException {
        if (data != null)
            return data;

        String result = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(DataUpdater.COMMON_XIAOLI, null);
        LogHelper.d(result);
        if (result == null)
            return null;
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

    public Calendar doRemap(Calendar calendar) {
        return data.doRemap(calendar);
    }
}
