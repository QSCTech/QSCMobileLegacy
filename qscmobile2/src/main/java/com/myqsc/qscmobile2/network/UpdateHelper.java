package com.myqsc.qscmobile2.network;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.Utility;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
                    mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                            .edit()
                            .putString(message.getData().getString("key"), (String) message.obj)
                            .commit();
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
