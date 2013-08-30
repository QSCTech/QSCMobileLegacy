package com.myqsc.qscmobile2.login;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.common.view.LoadingFragment;
import com.myqsc.qscmobile2.exam.ExamActivity;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.uti.Utility;
import com.myqsc.qscmobile2.xiaoche.XiaocheActivity;
import com.umeng.analytics.MobclickAgent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity {
	EditText uid = null;
	EditText pwd = null;
	Button btn = null;
	Activity activity = null;

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		activity = this;
		AwesomeFontHelper.setFontFace((TextView)findViewById(R.id.login_icon_user), this);
		AwesomeFontHelper.setFontFace((TextView)findViewById(R.id.login_icon_pwd), this);
		
		uid = (EditText) findViewById(R.id.login_activity_uid);
		pwd = (EditText) findViewById(R.id.login_activity_pwd);
		btn = (Button) findViewById(R.id.login_activity_btn);
		
		uid.addTextChangedListener(myTextWatcher);
		pwd.addTextChangedListener(myTextWatcher);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				
				CheckUserAsyncTask task = new CheckUserAsyncTask(uid.getText().toString(), pwd.getText().toString(), getBaseContext()) {
					
					@Override
					public void onHandleMessage(Message message) {
						if (message.what == 0){
							Toast.makeText(mContext, (CharSequence) message.obj, Toast.LENGTH_LONG).show();
						}
						if (message.what == 1){
							Toast.makeText(mContext, "登陆成功", Toast.LENGTH_LONG).show();
							activity.finish();
							activity.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
						}
					}
				};
				if (android.os.Build.VERSION.SDK_INT >= 15)
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				else
					task.execute();
				
			}
		});
		
	}

	final TextWatcher myTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (uid.getEditableText().length() == 0 || pwd.getEditableText().length() == 0){
				btn.setBackgroundResource(R.drawable.login_btn2);
			} else {
				btn.setBackgroundResource(R.drawable.login_btn1);
			}
		}
	};
	
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		activity.finish();
		activity.overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
