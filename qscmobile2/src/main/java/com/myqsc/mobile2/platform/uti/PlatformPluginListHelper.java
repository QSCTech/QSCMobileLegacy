package com.myqsc.mobile2.platform.uti;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.fragment.uti.PluginListInitCallback;
import com.myqsc.mobile2.platform.update.PlatformUpdateHelper;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;

import java.util.List;
import java.util.Vector;

/**
 * Created by richard on 13-10-24.
 */
public class PlatformPluginListHelper {

    PluginListInitCallback callback = null;

    public void setPluginListInitCallback (PluginListInitCallback callback) {
        this.callback = callback;
    }

    public void initList(final Activity activity) {
        final SharedPreferences preferences = activity.getSharedPreferences(Utility.PREFERENCE, 0);
        String listData = preferences.getString(PlatformUpdateHelper.PLUGIN_LIST_RAW, null);
        final Vector<PluginStructure> localPluginList = PlatformUpdateHelper.parsePluginList(listData);
        if (localPluginList != null)
            callback.initList(localPluginList);
            //若本地插件列表不为空则初始化本地插件列表

        //从网上加载插件列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                String pluginListStr = PlatformUpdateHelper.syncGetPluginList();
                final Vector<PluginStructure> pluginList = PlatformUpdateHelper.parsePluginList(
                    pluginListStr
                );
                //如果网上下载是空的就不保存了
                if (pluginList == null)
                    return ;
                preferences.edit()
                        .putString(PlatformUpdateHelper.PLUGIN_LIST_RAW, pluginListStr)
                        .commit();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.initList(pluginList);
                    }
                });
            }
        }).start();
    }
}
