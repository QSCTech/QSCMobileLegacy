package com.myqsc.mobile2.Notice;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-10-8.
 */
public class NoticeActivity extends SwipeBackActivity {
    LinearLayout linearLayout = null;
    PullToRefreshScrollView scrollView = null;
    LayoutInflater mInflater = null;
    NoticeHelper noticeHelper = null;

    public static final int SELECT_TINT = 0;
    public static final int SELECT_FIRE = 1;
    public static final int SELECT_SEARCH = 2;

    final Handler handler = new Handler();

    private int selected = 0;

    private final static int[] SELECT = {
        SELECT_TINT, SELECT_FIRE, SELECT_SEARCH
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(this);
        setContentView(R.layout.activity_notice);

        AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.notice_icon_fire), this);
        AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.notice_icon_search), this);
        AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.notice_icon_tint), this);

        findViewById(R.id.notice_tint)
                .setOnClickListener(onSelectChangedListener);
        findViewById(R.id.notice_fire)
                .setOnClickListener(onSelectChangedListener);
        findViewById(R.id.notice_search)
                .setOnClickListener(onSelectChangedListener);

        linearLayout = (LinearLayout) findViewById(R.id.activity_notice_linear);

        scrollView = (PullToRefreshScrollView) findViewById(R.id.activity_notice_scroll);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                noticeHelper.getMore(selected, handler);
            }
        });

        noticeHelper = new NoticeHelper(linearLayout, scrollView, this);
        noticeHelper.getMore(selected, handler);
        setSelected();
    }

    private void setSelected() {
        ((TextView) findViewById(R.id.notice_icon_tint)).setTextColor(getResources().getColor(R.color.gray_text));
        ((TextView) findViewById(R.id.notice_icon_fire)).setTextColor(getResources().getColor(R.color.gray_text));
        ((TextView) findViewById(R.id.notice_icon_search)).setTextColor(getResources().getColor(R.color.gray_text));

        ((TextView) findViewById(R.id.notice_icon_tint_text)).setTextColor(getResources().getColor(R.color.gray_text));
        ((TextView) findViewById(R.id.notice_icon_fire_text)).setTextColor(getResources().getColor(R.color.gray_text));
        ((TextView) findViewById(R.id.notice_icon_search_text)).setTextColor(getResources().getColor(R.color.gray_text));

        switch (selected) {
            case SELECT_TINT:
                ((TextView) findViewById(R.id.notice_icon_tint)).setTextColor(getResources().getColor(R.color.blue_text));
                ((TextView) findViewById(R.id.notice_icon_tint_text)).setTextColor(getResources().getColor(R.color.blue_text));
                break;
            case SELECT_FIRE:
                ((TextView) findViewById(R.id.notice_icon_fire)).setTextColor(getResources().getColor(R.color.blue_text));
                ((TextView) findViewById(R.id.notice_icon_fire_text)).setTextColor(getResources().getColor(R.color.blue_text));
                break;
            case SELECT_SEARCH:
                ((TextView) findViewById(R.id.notice_icon_search)).setTextColor(getResources().getColor(R.color.blue_text));
                ((TextView) findViewById(R.id.notice_icon_search_text)).setTextColor(getResources().getColor(R.color.blue_text));
                break;
        }
    }

    final View.OnClickListener onSelectChangedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.notice_tint:
                    if (selected == SELECT_TINT)
                        break;

                    scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    linearLayout.removeAllViews();
                    selected = SELECT_TINT;
                    setSelected();
                    noticeHelper.reset();
                    noticeHelper.getMore(selected, handler);
                    break;

                case R.id.notice_fire:
                    if (selected == SELECT_FIRE)
                        break;

                    scrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                    linearLayout.removeAllViews();
                    selected = SELECT_FIRE;
                    setSelected();
                    noticeHelper.reset();
                    noticeHelper.getMore(selected, handler);
                    break;

                case R.id.notice_search:

                    scrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                    linearLayout.removeAllViews();
                    selected = SELECT_SEARCH;
                    setSelected();
                    noticeHelper.reset();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
