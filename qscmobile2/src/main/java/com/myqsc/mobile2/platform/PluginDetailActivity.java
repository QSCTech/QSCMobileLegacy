package com.myqsc.mobile2.platform;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.platform.JSInterface.JSInterface;
import com.myqsc.mobile2.platform.update.PlatformUpdateHelper;
import com.myqsc.mobile2.platform.uti.PluginStructure;
import com.myqsc.mobile2.uti.LogHelper;

import java.io.File;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-10-27.
 */
public class PluginDetailActivity extends SwipeBackActivity {

    WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_activity);

        String ID = getIntent().getStringExtra("ID");

        if (ID == null) {
            finish();
            return ;
        }

        PluginStructure structure = new PluginStructure(ID, this);

        webView = (WebView) findViewById(R.id.card_webview);

        webView.getSettings().setJavaScriptEnabled(true);

        JSInterface jsInterface = new JSInterface(this, webView, null);
        jsInterface.init();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                LogHelper.e(consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });

        LogHelper.e("file:" +
                new File(getFilesDir(), PlatformUpdateHelper.PATH_ADD + structure.path + "/index.html")
                        .getAbsolutePath());

        webView.loadUrl(
                "file:" +
                new File(getFilesDir(), PlatformUpdateHelper.PATH_ADD + structure.path + "/index.html")
                        .getAbsolutePath()
        );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack())
                webView.goBack();
            else
                scrollToFinishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
