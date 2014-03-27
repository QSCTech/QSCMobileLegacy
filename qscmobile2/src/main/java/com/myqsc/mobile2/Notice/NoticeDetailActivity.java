package com.myqsc.mobile2.Notice;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myqsc.mobile2.Notice.Fragment.NoticeImageHelper;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.fragment.MySwipeExitActivity;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.umeng.analytics.MobclickAgent;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-10-9.
 */
public class NoticeDetailActivity extends MySwipeExitActivity {
    LinearLayout linearLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        AwesomeFontHelper.setFontFace(
                (TextView) findViewById(R.id.notice_detail_globe_icon),
                this
        );
        AwesomeFontHelper.setFontFace(
                (TextView) findViewById(R.id.notice_detail_share_icon),
                this
        );

        linearLayout = (LinearLayout) findViewById(R.id.activity_notice_linear);

        NoticeDetailHelper helper = new NoticeDetailHelper(linearLayout);
        helper.setShareItem(findViewById(R.id.notice_detail_share));
        helper.setOpenItem(findViewById(R.id.notice_detail_globe));

        int id = getIntent().getIntExtra("id", 1);

        ((AnimationDrawable) findViewById(R.id.loading_image)
                .getBackground()).start();

        helper.getEvent(id, null);
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
