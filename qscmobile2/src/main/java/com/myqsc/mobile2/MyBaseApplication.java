package com.myqsc.mobile2;

import android.app.Application;
import android.content.Context;

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

    public static Context getAppContext(){
        return MyBaseApplication.context;
    }
}
