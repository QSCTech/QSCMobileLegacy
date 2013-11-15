package com.myqsc.mobile2.platform.JSInterface;

import android.content.SharedPreferences;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by richard on 13-9-8.
 */
public class JSInterfaceKVDB {
    public static void set (final String pluginID, final String args,
                            final String callback,
                            final WebView webView) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(args);

                    SharedPreferences.Editor editor = webView.getContext()
                            .getSharedPreferences("plugin" + pluginID, 0)
                            .edit();
                    editor.putString(jsonObject.getString("key"), jsonObject.getString("value"));
                    editor.commit();

                    JSInterfaceCallback.successCallback(callback, null, webView);
                } catch (Exception e) {
                    e.printStackTrace();
                    JSInterfaceCallback.failedCallback(callback, null, webView);
                }
            }
        });
    }

    public static void get(final String pluginID, final String args,
                           final String callback,
                           final WebView webView) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(args);

                    String result = webView.getContext()
                            .getSharedPreferences("plugin" + pluginID, 0)
                            .getString(jsonObject.getString("key"), null);
                    JSInterfaceCallback.successCallback(callback, result, webView);
                } catch (Exception e) {
                    e.printStackTrace();
                    JSInterfaceCallback.failedCallback(callback, null, webView);
                }
            }
        });
    }

    public static void clear(final String pluginID, final String args,
                             final String callback,
                             final WebView webView) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    webView.getContext().getSharedPreferences("plugin" + pluginID, 0)
                            .edit()
                            .clear()
                            .commit();
                    JSInterfaceCallback.successCallback(callback, null, webView);
                } catch (Exception e) {
                    e.printStackTrace();
                    JSInterfaceCallback.failedCallback(callback, null, webView);
                }
            }
        });
    }

    public static void remove(final String pluginID, final String args,
                              final String callback,
                              final WebView webView) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(args);
                    SharedPreferences.Editor editor = webView
                            .getContext()
                            .getSharedPreferences("plugin" + pluginID, 0)
                            .edit();
                    editor.remove(jsonObject.getString("key"));
                    editor.commit();
                    JSInterfaceCallback.successCallback(callback, null, webView);
                } catch (JSONException e) {
                    e.printStackTrace();
                    JSInterfaceCallback.failedCallback(callback, null, webView);
                }
            }
        });
    }
}
