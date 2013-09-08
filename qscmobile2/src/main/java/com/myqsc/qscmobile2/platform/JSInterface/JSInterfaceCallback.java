package com.myqsc.qscmobile2.platform.JSInterface;

import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by richard on 13-9-8.
 */
public class JSInterfaceCallback {
    public static void successCallback(String name, String reason, WebView webView) {
        if (reason == null)
            webView.loadUrl("javascript: " + name + "();");
        else
            webView.loadUrl("javascript: " + name + "(" + reason);
    }

    public static void failedCallback(String name, String reason, WebView webView) {
        if (reason == null)
            reason = "通用错误：参数解析失败";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("error", reason);
            webView.loadUrl("javascript: " + name + "(" + jsonObject.toString() + ");");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
