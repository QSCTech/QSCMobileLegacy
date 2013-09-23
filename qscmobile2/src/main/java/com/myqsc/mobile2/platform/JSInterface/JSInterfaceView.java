package com.myqsc.mobile2.platform.JSInterface;

import android.os.Bundle;
import android.os.Message;
import android.webkit.WebView;

import org.json.JSONObject;

/**
 * Created by richard on 13-9-8.
 */
public class JSInterfaceView {
    public static void card(final String pluginID, final String args,
                            final String callback,
                            final WebView webView) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(args);
                    String title = jsonObject.getString("title");
                    String content = jsonObject.getString("content");
                    Message message = webView.getHandler().obtainMessage();
                    message.what = JSInterface.JS_SUCCESS;
                    message.arg1 = JSInterface.VIEW_CARD;

                    Bundle bundle = new Bundle();
                    bundle.putString("ID", pluginID);
                    bundle.putString("title", title);
                    bundle.putString("content", content);
                    message.setData(bundle);

                    message.sendToTarget();

                    JSInterfaceCallback.successCallback(callback, null, webView);
                } catch (Exception e) {
                    e.printStackTrace();
                    JSInterfaceCallback.failedCallback(callback, null, webView);
                }
            }
        });
    }
}
