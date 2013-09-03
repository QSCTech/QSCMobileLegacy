package com.myqsc.qscmobile2.network;

import android.content.Context;
import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by richard on 13-9-3.
 */
public class UpdateHelper {
    Context mContext = null;
    public UpdateHelper(Context context) {
        this.mContext = context;
    }

    public void UpdateAll(final Handler handler){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        DataUpdater dataUpdater = new DataUpdater();

        for(String key : DataUpdater.name.keySet()) {
            executorService.submit(new DataUpdaterRunnable(key, handler));
        }

    }
}
