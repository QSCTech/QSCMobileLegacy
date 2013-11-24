package com.myqsc.mobile2.exam;


import com.myqsc.mobile2.R;
import com.myqsc.mobile2.exam.fragment.AllExamFragment;
import com.myqsc.mobile2.exam.fragment.EveryDayExamFragment;
import com.myqsc.mobile2.fragment.MySwipeExitActivity;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ExamActivity extends MySwipeExitActivity {
	int check = 1;

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
    }

    TextView icon_right, text_right, icon_left, text_left;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam);
		AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.exam_icon_left), this);
		AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.exam_icon_right), this);
		
		icon_right 	= (TextView) findViewById(R.id.exam_icon_right);
		icon_left 	= (TextView) findViewById(R.id.exam_icon_left);
		text_right	= (TextView) findViewById(R.id.exam_text_right);
		text_left	= (TextView) findViewById(R.id.exam_text_left);
		
		manager = getSupportFragmentManager();
		setTextColor(check);
		
		((LinearLayout)findViewById(R.id.exam_linear_left)).setOnClickListener(onClickListener);
		((LinearLayout)findViewById(R.id.exam_linear_right)).setOnClickListener(onClickListener);
	}
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.exam_linear_left)
				check = 0;
			if (v.getId() == R.id.exam_linear_right)
				check = 1;
			setTextColor(check);
		}
	};
	
	private void setTextColor(int check){
		if (check == 0){
			icon_right.setTextColor(getResources().getColor(R.color.black_text));
			text_right.setTextColor(getResources().getColor(R.color.black_text));
			
			icon_left.setTextColor(getResources().getColor(R.color.blue_text));
			text_left.setTextColor(getResources().getColor(R.color.blue_text));

            FragmentTransaction transaction = manager.beginTransaction();
            Fragment fragment = manager.findFragmentByTag("exam_everyday");
            if (fragment == null)
                fragment = new EveryDayExamFragment();
            transaction.replace(R.id.activity_exam_fragment, fragment, "exam_everyday");
            transaction.commitAllowingStateLoss();
		} 
		if (check == 1){
			icon_left.setTextColor(getResources().getColor(R.color.black_text));
			text_left.setTextColor(getResources().getColor(R.color.black_text));
			
			icon_right.setTextColor(getResources().getColor(R.color.blue_text));
			text_right.setTextColor(getResources().getColor(R.color.blue_text));

            FragmentTransaction transaction = manager.beginTransaction();
            Fragment fragment = manager.findFragmentByTag("exam_all");
            if (fragment == null)
                fragment = new AllExamFragment();
            transaction.replace(R.id.activity_exam_fragment, fragment, "exam_all");
            transaction.commitAllowingStateLoss();
		}
	}

    FragmentManager manager = null;

}
