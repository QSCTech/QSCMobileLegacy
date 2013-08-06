package com.myqsc.qscmobile2.login;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.common.view.LoadingFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.annotation.SuppressLint;
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
import android.widget.Toast;

public class LoginActivity extends FragmentActivity {
	EditText uid = null;
	EditText pwd = null;
	Button btn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		uid = (EditText) findViewById(R.id.login_activity_uid);
		pwd = (EditText) findViewById(R.id.login_activity_pwd);
		btn = (Button) findViewById(R.id.login_activity_btn);
		
		uid.addTextChangedListener(myTextWatcher);
		pwd.addTextChangedListener(myTextWatcher);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				
				final View frame = findViewById(R.id.login_activity_frame);
				
				final RelativeLayout layout = (RelativeLayout) findViewById(R.id.login_acitivity_main_layout);
				final Animation animation = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.fade_out);
				animation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					@Override
					public void onAnimationEnd(Animation animation) {
						layout.setVisibility(View.GONE);
						frame.setVisibility(View.VISIBLE);
					}
				});
				layout.startAnimation(animation);
				
				final FragmentManager manager = getSupportFragmentManager();
				final FragmentTransaction transaction = manager.beginTransaction();
				transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
				final LoadingFragment loadingFragment = new LoadingFragment();
				transaction.replace(R.id.login_activity_frame, loadingFragment);
				transaction.commit();
				
				CheckUserAsyncTask task = new CheckUserAsyncTask(uid.getText().toString(), pwd.getText().toString(), getBaseContext()) {
					
					@Override
					public void onHandleMessage(Message message) {
						if (message.what == 0){
							Toast.makeText(mContext, (CharSequence) message.obj, Toast.LENGTH_LONG).show();
						}
						if (message.what == 1){
							Toast.makeText(mContext, "登陆成功", Toast.LENGTH_LONG).show();
						}
						
						final Animation showAnimation = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.fade_in);
						showAnimation.setAnimationListener(new Animation.AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
							}
							@Override
							public void onAnimationRepeat(Animation animation) {
							}
							@Override
							public void onAnimationEnd(Animation animation) {
								layout.setVisibility(View.VISIBLE);
								frame.setVisibility(View.GONE);
							}
						});
						layout.startAnimation(showAnimation);
					}
				};
				if (android.os.Build.VERSION.SDK_INT >= 15){
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					task.execute();
				};
				
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
