package com.myqsc.qscmobile2.curriculum;


import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.TextView;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.curriculum.fragment.KebiaoEverydayFragment;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class CurriculumActivity extends SwipeBackActivity {
    FragmentManager manager = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            overridePendingTransition(0, R.anim.right_push_out);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curriculum);
        manager = getSupportFragmentManager();
        AwesomeFontHelper.setFontFace((TextView)findViewById(R.id.curriculum_everyday), this);
        AwesomeFontHelper.setFontFace((TextView)findViewById(R.id.curriculum_allweek), this);

        FragmentTransaction transaction = manager.beginTransaction();
        KebiaoEverydayFragment fragment = new KebiaoEverydayFragment();
        transaction.add(R.id.curriculum_frame, fragment);
        transaction.commit();
    }

}



