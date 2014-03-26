package com.myqsc.mobile2.platform.JSInterface;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.myqsc.mobile2.uti.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by richard on 13-9-7.
 */
public class JSInterface {
    Activity activity = null;
    WebView webView = null;
    public SharedPreferences preferences = null;
    JSInterface jsInterface = null;

    public JSInterface(Activity activity, WebView webView, SharedPreferences preferences) {
        this.activity = activity;
        this.webView = webView;
        this.jsInterface = this;
        if (preferences == null)
            this.preferences = activity.getSharedPreferences("plugin", 0);
        else
            this.preferences = preferences;
    }

    public void init() {
//        webView.addJavascriptInterface(this, "QSCAndroid");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogHelper.d(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    public void sendRequest (final String result) {
        LogHelper.e(result);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);

                    String fn = jsonObject.getString("fn");
                    String pluginID = jsonObject.getString("pluginID");
                    String args = jsonObject.optString("args", null);
                    String callback = jsonObject.getString("callback");

                    if (fn.contains("view.")) {
                        if (fn.contains(".card"))
                            JSInterfaceView.card(
                                    pluginID,
                                    args,
                                    callback,
                                    webView,
                                    preferences
                            );
                    }

                    if (fn.contains("kvdb.")) {
                        if (fn.contains(".set"))
                            JSInterfaceKVDB.set(
                                    pluginID,
                                    args,
                                    callback,
                                    webView
                            );

                        if (fn.contains(".get"))
                            JSInterfaceKVDB.get(
                                    pluginID,
                                    args,
                                    callback,
                                    webView
                            );

                        if (fn.contains(".remove"))
                            JSInterfaceKVDB.remove(
                                    pluginID,
                                    args,
                                    callback,
                                    webView
                            );

                        if (fn.contains(".clear"))
                            JSInterfaceKVDB.clear(
                                    pluginID,
                                    args,
                                    callback,
                                    webView
                            );
                    }

                    if (fn.contains("user.")){
                        if (fn.contains(".stuid"))
                            JSInterfaceUser.stuid(
                                    pluginID,
                                    args,
                                    callback,
                                    webView
                            );

                        if (fn.contains(".pwd"))
                            JSInterfaceUser.pwd(
                                    pluginID,
                                    args,
                                    callback,
                                    webView
                            );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
