package com.myqsc.mobile2.network;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.myqsc.mobile2.MyBaseApplication;
import com.myqsc.mobile2.uti.BroadcastHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by richard on 13-9-3.
 */
public class UpdateHelper {
    Context mContext = null;
    ExecutorService executorService = null;
    public UpdateHelper(Context context) {
        this.mContext = context;
        executorService = Executors.newFixedThreadPool(3);
    }

    public void UpdateAll(){
        final int[] len = {DataUpdater.name.size()};
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.obj != null) {
                    --len[0];
                    LogHelper.d(message.getData().getString("key") + "update finished");

                    if (!messageChecker((String) message.obj)) {
                        mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                                .edit()
                                .putString(message.getData().getString("key"), (String) message.obj)
                                .commit();
                    }
                    Intent intent = new Intent(BroadcastHelper.BROADCAST_CARD_REDRAW);
                    intent.putExtra("card", message.getData().getString("key"));
                    mContext.sendBroadcast(intent);
                    //更新完后通知所有卡片重绘
                }

                if (len[0] == 0) {
                    Intent intent = new Intent(BroadcastHelper.BROADCAST_ALL_UPDATED);
                    mContext.sendBroadcast(intent);
                }
                return true;
            }
        });
        for(String key : DataUpdater.name.keySet()) {
            executorService.submit(new DataUpdaterRunnable(key,
                    handler,
                    mContext));
        }
    }

    /**
     * 判断这个json 中是否有msg，有的话toast出来，并返回true，没有返回false
     * @param data
     * @return
     */
    public static boolean messageChecker(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String message = jsonObject.getString("msg");

            if (MyBaseApplication.getAppContext() != null) {
                Toast.makeText(MyBaseApplication.getAppContext(), message, Toast.LENGTH_LONG).show();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void pullToRefresh(final Handler handler){
        final int[] len = {DataUpdater.name.size()};
        for (String key : DataUpdater.name.keySet()) {
            executorService.submit(new DataUpdaterRunnable(key, handler, mContext));
        }
    }

    public void update(final Handler handler, final String key) {
        executorService.submit(new DataUpdaterRunnable(key,
                handler,
                mContext));
    }
}
