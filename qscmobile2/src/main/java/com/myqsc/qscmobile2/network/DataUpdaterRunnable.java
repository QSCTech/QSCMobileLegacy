package com.myqsc.qscmobile2.network;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by richard on 13-9-3.
 */
public class DataUpdaterRunnable implements Runnable {
    String key = null;
    Handler handler = null;
    Context context = null;

    public DataUpdaterRunnable(String key,
                               Handler handler,
                               Context context) {
        this.key = key;
        this.handler = handler;
        this.context = context;
    }
    @Override
    public void run() {
        Message message = handler.obtainMessage(0);

        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        message.setData(bundle);

        message.obj = DataUpdater.update(key, context);
        message.sendToTarget();
    }
}