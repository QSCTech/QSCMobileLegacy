package com.myqsc.mobile2.Notice;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.myqsc.mobile2.Notice.Fragment.NoticeImageHelper;
import com.myqsc.mobile2.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-10-18.
 */
public class NoticeImageActivity extends SwipeBackActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_pic);
        String url = getIntent().getStringExtra("url");
        if (url == null) {
            finish();
            return;
        }
        NoticeImageHelper.initPic(url, (ImageView) findViewById(R.id.notice_cover_image));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            scrollToFinishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
