package com.myqsc.mobile2.Notice;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
    Context mContext = null;

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
        mContext = this;
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
        scrollView.setOnRefreshListener(onTintRefreshListener);

        noticeHelper = new NoticeHelper(linearLayout, scrollView, this);
        noticeHelper.getMore(selected);
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
                    scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    scrollView.setOnRefreshListener(onTintRefreshListener);

                    linearLayout.removeAllViews();
                    selected = SELECT_TINT;
                    setSelected();
                    noticeHelper.reset();
                    noticeHelper.getMore(selected);

                    break;

                case R.id.notice_fire:
                    scrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                    linearLayout.removeAllViews();
                    selected = SELECT_FIRE;
                    setSelected();
                    noticeHelper.reset();
                    noticeHelper.getMore(selected);
                    break;

                case R.id.notice_search:

                    scrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                    linearLayout.removeAllViews();
                    selected = SELECT_SEARCH;
                    setSelected();
                    noticeHelper.reset();

                    final View searchView = mInflater.inflate(R.layout.notice_search, null);
                    AwesomeFontHelper.setFontFace((TextView) searchView.findViewById(R.id.notice_search_icon), mContext);
                    ((EditText) searchView.findViewById(R.id.notice_search_edittext))
                            .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                @Override
                                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                    if (i == EditorInfo.IME_ACTION_SEARCH) {
                                        //在输入法上按了搜索键
                                        doSearch(((EditText) ((EditText) searchView.findViewById(R.id.notice_search_edittext)))
                                                .getText().toString());
                                        return true;
                                    }
                                    return false;
                                }
                            });

                    searchView.findViewById(R.id.notice_search_icon)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //点击那个搜索图标搜索
                                    doSearch(((EditText) ((EditText) searchView.findViewById(R.id.notice_search_edittext)))
                                            .getText().toString());
                                }
                            });
                    linearLayout.addView(searchView);
                    break;
            }
        }
    };

    private void doSearch(final String key) {
        selected = SELECT_SEARCH;
        setSelected();
        
        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                noticeHelper.getSearchResult(handler, key);
            }
        });
        noticeHelper.reset();
        noticeHelper.getSearchResult(handler, key);
    }

    final PullToRefreshBase.OnRefreshListener onTintRefreshListener = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            noticeHelper.getMore(selected);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        linearLayout.removeAllViews();
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
