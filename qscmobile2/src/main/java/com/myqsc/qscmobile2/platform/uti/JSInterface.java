package com.myqsc.qscmobile2.platform.uti;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by richard on 13-9-7.
 */
public class JSInterface {
    WebView webView = null;

    public final static int JS_FAILED = 0, JS_SUCCESS = 1;
    public final static int VIEW_CARD = 1;

    public JSInterface(WebView webView) {
        this.webView = webView;
    }

    @JavascriptInterface
    public void sendMessage(String result) {
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
                            webView
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
}
