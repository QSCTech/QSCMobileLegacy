package com.myqsc.mobile2.platform;

import android.os.Bundle;
import android.webkit.WebView;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.platform.JSInterface.JSInterface;
import com.myqsc.mobile2.platform.update.PlatformUpdateHelper;

import java.io.File;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-10-27.
 */
public class PluginDetailActivity extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_activity);

        String ID = getIntent().getStringExtra("ID");

        WebView webView = (WebView) findViewById(R.id.card_webview);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new JSInterface(), "QSCAndroid");

        webView.loadUrl(
                new File(getFilesDir(), PlatformUpdateHelper.PATH_ADD + "qiuShiGou" + '/' + "index.html")
                        .getAbsolutePath()
        );
    }
}
