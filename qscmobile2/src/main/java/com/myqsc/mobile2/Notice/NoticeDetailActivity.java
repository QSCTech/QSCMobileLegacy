package com.myqsc.mobile2.Notice;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.myqsc.mobile2.R;
import com.umeng.analytics.MobclickAgent;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-10-9.
 */
public class NoticeDetailActivity extends SwipeBackActivity {
    LinearLayout linearLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        linearLayout = (LinearLayout) findViewById(R.id.activity_notice_linear);

        NoticeDetailHelper helper = new NoticeDetailHelper(linearLayout);

        int id = getIntent().getIntExtra("id", 1);

        helper.getEvent(id);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            scrollToFinishActivity();
            return true;
        }
        return false;
    }
}
