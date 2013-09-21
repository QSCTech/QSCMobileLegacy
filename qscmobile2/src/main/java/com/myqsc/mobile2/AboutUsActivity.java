package com.myqsc.mobile2;

import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.io.InputStream;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class AboutUsActivity extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        WebView webView = (WebView) findViewById(R.id.about_us_text);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl("file:///android_asset/aboutus.html");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            scrollToFinishActivity();
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }
}
