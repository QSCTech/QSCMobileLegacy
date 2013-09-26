package com.myqsc.mobile2.Grade;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.myqsc.mobile2.Grade.Fragment.GradeClassFragment;
import com.myqsc.mobile2.Grade.Fragment.GradeTermFragment;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.AwesomeFontHelper;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-9-25.
 */
public class GradeActivity extends SwipeBackActivity {
    int type = 0;
    FragmentManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.activity_grade_right_icon), this);
        AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.activity_grade_left_icon), this);

        findViewById(R.id.activity_grade)
                .setBackgroundColor(getResources().getColor(R.color.white));

        manager = getSupportFragmentManager();
        changeFragment();

        findViewById(R.id.activity_grade_left)
                .setOnClickListener(onClickListener);
        findViewById(R.id.activity_grade_right)
                .setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.activity_grade_left)
                type = 0;
            if (view.getId() == R.id.activity_grade_right)
                type = 1;
            changeFragment();
        }
    };

    private void changeFragment() {
        if (type == 0) {
            ((TextView) findViewById(R.id.activity_grade_right_icon))
                    .setTextColor(getResources().getColor(R.color.gray));
            ((TextView) findViewById(R.id.activity_grade_right_text))
                    .setTextColor(getResources().getColor(R.color.gray));

            ((TextView) findViewById(R.id.activity_grade_left_text))
                    .setTextColor(getResources().getColor(R.color.blue_text));
            ((TextView) findViewById(R.id.activity_grade_left_icon))
                    .setTextColor(getResources().getColor(R.color.blue_text));

            Fragment fragment = manager.findFragmentByTag("single");
            if (fragment == null)
                fragment = new GradeClassFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.activity_grade_fragment, fragment, "single");
            transaction.commitAllowingStateLoss();
        }

        if (type == 1) {
            ((TextView) findViewById(R.id.activity_grade_right_icon))
                    .setTextColor(getResources().getColor(R.color.blue_text));
            ((TextView) findViewById(R.id.activity_grade_right_text))
                    .setTextColor(getResources().getColor(R.color.blue_text));

            ((TextView) findViewById(R.id.activity_grade_left_text))
                    .setTextColor(getResources().getColor(R.color.gray));
            ((TextView) findViewById(R.id.activity_grade_left_icon))
                    .setTextColor(getResources().getColor(R.color.gray));

            Fragment fragment = manager.findFragmentByTag("term");
            if (fragment == null)
                fragment = new GradeTermFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.activity_grade_fragment, fragment, "term");
            transaction.commitAllowingStateLoss();
        }
    }




}
