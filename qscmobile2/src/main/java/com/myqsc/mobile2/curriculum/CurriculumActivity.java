package com.myqsc.mobile2.curriculum;


import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.curriculum.fragment.KebiaoEverydayFragment;
import com.umeng.analytics.MobclickAgent;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class CurriculumActivity extends SwipeBackActivity {
    FragmentManager manager = null;

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        KebiaoEverydayFragment fragment = new KebiaoEverydayFragment();
        transaction.add(R.id.curriculum_frame, fragment);
        transaction.commitAllowingStateLoss();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curriculum);
    }

}



