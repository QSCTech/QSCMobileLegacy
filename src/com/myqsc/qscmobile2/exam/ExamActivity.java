package com.myqsc.qscmobile2.exam;

import java.io.Serializable;
import java.util.List;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.R.color;
import com.myqsc.qscmobile2.exam.uti.ExamAdapter;
import com.myqsc.qscmobile2.exam.uti.UpdateExamAsyncTask;
import com.myqsc.qscmobile2.login.UserSwitchActivity;
import com.myqsc.qscmobile2.support.database.structure.ExamStructure;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;
import com.myqsc.qscmobile2.uti.PersonalDataHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ExamActivity extends FragmentActivity {
	int check = 1;
	
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
		
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		
		AllExamFragment fragment = new AllExamFragment(this);
		transaction.add(R.id.exam_linear_all, fragment);
		transaction.commit();
		
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
		} 
		if (check == 1){
			icon_left.setTextColor(getResources().getColor(R.color.black_text));
			text_left.setTextColor(getResources().getColor(R.color.black_text));
			
			icon_right.setTextColor(getResources().getColor(R.color.blue_text));
			text_right.setTextColor(getResources().getColor(R.color.blue_text));
		}
	}

}
