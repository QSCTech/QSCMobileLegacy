package com.myqsc.qscmobile2.login;

import com.google.analytics.tracking.android.EasyTracker;
import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.login.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.network.DataUpdater;
import com.myqsc.qscmobile2.network.UpdateHelper;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.umeng.analytics.MobclickAgent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
        unregisterReceiver(updateAllReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);

        IntentFilter intentFilter = new IntentFilter(BroadcastHelper.BROADCAST_ALL_UPDATED);
        registerReceiver(updateAllReceiver, intentFilter);
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

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 0)
                    Toast.makeText(getApplicationContext(),
                            (CharSequence) message.obj,
                            Toast.LENGTH_LONG).show();
                else {
                    PersonalDataHelper helper = new PersonalDataHelper(getApplicationContext());
                    helper.add(new UserIDStructure(uid.getText().toString(),
                            pwd.getText().toString(),
                            true));
                    sendBroadcast(new Intent(BroadcastHelper.BROADCAST_USER_CHANGED));
                    UpdateHelper updateHelper = new UpdateHelper(getApplicationContext());
                    updateHelper.UpdateAll();
                }
                return true;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = handler.obtainMessage();
                        try {
                            String result = DataUpdater.get(
                                    DataUpdater.name.get(DataUpdater.JW_VALIDATE) +
                                        "?stuid=" + uid.getText().toString() +
                                        "&pwd=" + pwd.getText().toString()
                            );
                            LogHelper.d(result);

                            if (result == null){
                                //网络错误
                                message.what = 0;
                                message.obj = "网络错误";
                            } else {
                                JSONObject jsonObject = new JSONObject(result);
                                jsonObject.getString("stuid");
                                message.what = 1;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            message.what = 0;
                            message.obj = "密码错误";
                        }
                        message.sendToTarget();
                    }
                }).start();
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

    private class UpdateAllReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    final BroadcastReceiver updateAllReceiver = new UpdateAllReceiver();
}
