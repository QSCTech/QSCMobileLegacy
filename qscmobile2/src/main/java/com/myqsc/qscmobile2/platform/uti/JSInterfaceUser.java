package com.myqsc.qscmobile2.platform.uti;

import android.webkit.WebView;

import com.myqsc.qscmobile2.login.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;

/**
 * Created by richard on 13-9-8.
 */
public class JSInterfaceUser {
    public static void stuid(final String pluginID, final String args,
                             final String callback,
                             final WebView webView) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    PersonalDataHelper helper = new PersonalDataHelper(webView.getContext());
                    UserIDStructure userIDStructure = helper.getCurrentUser();
                    JSInterfaceCallback.successCallback(callback, userIDStructure.uid, webView);
                } catch (Exception e) {
                    e.printStackTrace();
                    JSInterfaceCallback.failedCallback(callback, null, webView);
                }
            }
        });
    }

    public static void pwd(final String pluginID, final String args,
                           final String callback,
                           final WebView webView) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    PersonalDataHelper helper = new PersonalDataHelper(webView.getContext());
                    UserIDStructure userIDStructure = helper.getCurrentUser();
                    JSInterfaceCallback.successCallback(callback, userIDStructure.pwd, webView);
                } catch (Exception e) {
                    e.printStackTrace();
                    JSInterfaceCallback.failedCallback(callback, null, webView);
                }
            }
        });
    }
}
