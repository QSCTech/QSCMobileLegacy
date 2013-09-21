package com.myqsc.mobile2;

import android.app.Application;
import android.content.Context;

/**
 * Created by richard on 13-9-21.
 */
public class MyBaseApplication extends Application {
    private static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext(){
        return MyBaseApplication.context;
    }
}
