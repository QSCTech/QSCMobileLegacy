package com.myqsc.mobile2.platform.JSInterface;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.webkit.WebView;

import org.json.JSONObject;

/**
 * Created by richard on 13-9-8.
 */
public class JSInterfaceView {
    public static final String viewCardPrefix = "_viewCardPrefix_";
    public static final String viewTitle = viewCardPrefix + "title";
    public static final String viewContent = viewCardPrefix + "content";

    public static void card(final String pluginID, final String args,
                            final String callback,
                            final WebView webView,
                            final SharedPreferences preferences) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(args);
                    String title = jsonObject.getString("title");
                    String content = jsonObject.getString("content");

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(pluginID + viewTitle, title);
                    editor.putString(pluginID + viewContent, content);
                    editor.commit();

                    JSInterfaceCallback.successCallback(callback, null, webView);
                } catch (Exception e) {
                    e.printStackTrace();
                    JSInterfaceCallback.failedCallback(callback, null, webView);
                }
            }
        });
    }
}
