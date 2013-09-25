package com.myqsc.mobile2.Grade;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.myqsc.mobile2.Grade.Fragment.GradeClassFragment;
import com.myqsc.mobile2.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-9-25.
 */
public class GradeActivity extends SwipeBackActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        findViewById(R.id.activity_grade)
                .setBackgroundColor(getResources().getColor(R.color.white));

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.activity_grade_fragment, new GradeClassFragment());
        transaction.commitAllowingStateLoss();
    }




}
