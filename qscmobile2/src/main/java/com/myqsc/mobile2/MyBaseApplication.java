package com.myqsc.mobile2;

import android.app.Application;
import android.content.Context;

import com.myqsc.mobile2.uti.LogHelper;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by richard on 13-9-21.
 */
@ReportsCrashes(
        formKey = "", // This is required for backward compatibility but not used
        formUri = "https://m.myqsc.com/api/v2/wireless/crash"
)
public class MyBaseApplication extends Application {
    private static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ACRA.init(this);
    }

    private static boolean startWithBroadcast = false;

    /**
     * 声明此次是从Broadcast启动（dirty hack）
     */
    public static void setStartWithBroadcast() {
        startWithBroadcast = true;
    }
    public static boolean isStartWithBroadcast() {
        return startWithBroadcast;
    }

    public static Context getAppContext(){
        return MyBaseApplication.context;
    }
}
