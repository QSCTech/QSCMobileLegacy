package com.myqsc.mobile2.ExtraFunction.ZJUWLANLogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.myqsc.mobile2.uti.LogHelper;

/**
 * Created by richard on 13-9-29.
 */
public class ZJUWLANConnectReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogHelper.d("ZJUWLAN LOGIN STARTED");

        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo.State state = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (NetworkInfo.State.CONNECTED == state) {

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            LogHelper.d(wifiInfo.getSSID());
            if (wifiInfo.getSSID().contains("ZJUWLAN")) {
                //启动登录服务
                LogHelper.d("ZJUWLAN Service Started");
                context.startService(new Intent(context, ZJUWLANConnectService.class));
            }
        }
    }
}
