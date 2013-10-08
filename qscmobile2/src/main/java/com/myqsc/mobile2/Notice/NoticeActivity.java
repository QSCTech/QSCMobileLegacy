package com.myqsc.mobile2.Notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.AwesomeFontHelper;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-10-8.
 */
public class NoticeActivity extends SwipeBackActivity {
    LinearLayout linearLayout = null;
    PullToRefreshScrollView scrollView = null;
    LayoutInflater mInflater = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(this);
        setContentView(R.layout.activity_notice);

        linearLayout = (LinearLayout) findViewById(R.id.activity_notice_linear);

        scrollView = (PullToRefreshScrollView) findViewById(R.id.activity_notice_scroll);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                add_view();
            }
        });
        add_view();
    }

    private void add_view() {
        for (int i = 0; i != 50; ++i) {
            if (scrollView.isRefreshing())
                scrollView.onRefreshComplete();
            View view = mInflater.inflate(R.layout.notice_bar, null);
            linearLayout.addView(view);
            AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.notice_bar_icon), this);
        }
    }
}
