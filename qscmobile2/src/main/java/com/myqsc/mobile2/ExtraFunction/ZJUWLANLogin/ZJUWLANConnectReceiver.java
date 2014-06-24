package com.myqsc.mobile2.ExtraFunction.ZJUWLANLogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.myqsc.mobile2.MyBaseApplication;
import com.myqsc.mobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.mobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.mobile2.curriculum.uti.KebiaoUtility;
import com.myqsc.mobile2.uti.LogHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by richard on 13-9-29.
 */
public class ZJUWLANConnectReceiver extends BroadcastReceiver {
    public static final String PREFER = "ZJUWLANConnectReceiver";
    public static final String HISTORY = "history_array";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogHelper.d("ZJUWLAN LOGIN STARTED");
        MyBaseApplication.setStartWithBroadcast();

        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo.State state = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (NetworkInfo.State.CONNECTED == state) {

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo == null) return;
            LogHelper.d(wifiInfo.getSSID());

            if (wifiInfo.getSSID().contains("ZJUWLAN")) {
                //启动登录服务
                LogHelper.d("ZJUWLAN Service Started");
                context.startService(new Intent(context, ZJUWLANConnectService.class));

                try {
                    SharedPreferences preferences = context.getSharedPreferences(PREFER, 0);
                    Set<String> history = preferences.getStringSet(HISTORY, new HashSet<String>());

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("BSSID", wifiInfo.getBSSID());
                    jsonObject.put("SSID", wifiInfo.getSSID());
                    jsonObject.put("MAC", wifiInfo.getMacAddress());
                    jsonObject.put("TIMESTAMP", (int) (Calendar.getInstance().getTimeInMillis() / 1000));

                    KebiaoDataHelper helper = new KebiaoDataHelper(context);
                    List<KebiaoClassData> list = helper.getDay(Calendar.getInstance());
                    Map map = KebiaoUtility.getDiffTime(Calendar.getInstance(), list);
                    if (map != null && (Integer) map.get(1) > 0) {
                        //有课且正在上课
                        KebiaoClassData kebiaoClassData = (KebiaoClassData) map.get(3);

                        jsonObject.put("COURSE_NAME", kebiaoClassData.name);
                        jsonObject.put("COURSE_POSITION", kebiaoClassData.place);
                    }
                    history.add(jsonObject.toString());

                    preferences.edit()
                            .putStringSet(HISTORY, history)
                            .commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
