package com.myqsc.mobile2.Notice;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.myqsc.mobile2.Notice.Fragment.NoticeImageHelper;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.fragment.MySwipeExitActivity;
import com.myqsc.mobile2.uti.LogHelper;
import com.umeng.analytics.MobclickAgent;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-10-18.
 */
public class NoticeImageActivity extends MySwipeExitActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_pic);
        String url = getIntent().getStringExtra("url");
        LogHelper.i(url);
        if (url == null) {
            scrollToFinishActivity();
            return;
        }
        NoticeImageHelper.initPic(url, (ImageView) findViewById(R.id.notice_cover_image));
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
    protected void onDestroy() {
        NoticeImageHelper.thread.interrupt();
        super.onDestroy();
    }
}
