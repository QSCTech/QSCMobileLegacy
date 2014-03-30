package com.myqsc.mobile2.fragment;

import android.view.KeyEvent;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-11-20.
 */
public class MySwipeExitActivity extends SwipeBackActivity {
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            scrollToFinishActivity();
            return true;
        }
        return false;
    }
}
