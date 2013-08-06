package com.myqsc.qscmobile2;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
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
