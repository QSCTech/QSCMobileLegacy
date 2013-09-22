package com.myqsc.mobile2.login;

import com.google.analytics.tracking.android.EasyTracker;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.fragment.LoadFragment;
import com.myqsc.mobile2.login.uti.PersonalDataHelper;
import com.myqsc.mobile2.network.DataUpdater;
import com.myqsc.mobile2.network.UpdateHelper;
import com.myqsc.mobile2.support.database.structure.UserIDStructure;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.uti.BroadcastHelper;
import com.myqsc.mobile2.uti.LogHelper;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

        ((Button)findViewById(R.id.login_activity_exit))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
                    }
                });
		
		uid.addTextChangedListener(myTextWatcher);
		pwd.addTextChangedListener(myTextWatcher);

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 0){
                    FragmentManager manager = getSupportFragmentManager();
                    if (manager.getBackStackEntryCount() != 0)
                        manager.popBackStack();
                    findViewById(R.id.login_acitivity_main_layout).setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(),
                            (CharSequence) message.obj,
                            Toast.LENGTH_LONG).show();
                }

                else {
                    PersonalDataHelper helper = new PersonalDataHelper(getApplicationContext());
                    helper.add(new UserIDStructure(uid.getText().toString(),
                            pwd.getText().toString()));
                    sendBroadcast(new Intent(BroadcastHelper.BROADCAST_USER_CHANGED));
                    UpdateHelper updateHelper = new UpdateHelper(getApplicationContext());
                    updateHelper.UpdateAll();

                    final FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    if (manager.findFragmentByTag("load") != null)
                        transaction.remove(manager.findFragmentByTag("load"));
                    transaction.add(R.id.login_frame, new LoadFragment(), "load");
                    transaction.setCustomAnimations(R.anim.fade_out, R.anim.fade_in);
                    transaction.commitAllowingStateLoss();
                }
                return true;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//隐藏输入法
                findViewById(R.id.login_frame).clearFocus();

                findViewById(R.id.login_acitivity_main_layout).setVisibility(View.INVISIBLE);
                final FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                if (manager.findFragmentByTag("load") != null)
                    transaction.remove(manager.findFragmentByTag("load"));
                transaction.add(R.id.login_frame, new LoadFragment(), "load");
                transaction.setCustomAnimations(R.anim.fade_out, R.anim.fade_in);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = handler.obtainMessage();
                        try {
                            String result = DataUpdater.get(
                                    DataUpdater.name.get(DataUpdater.JW_VALIDATE) +
                                        "?stuid=" + URLEncoder.encode(uid.getText().toString(), "UTF-8") +
                                        "&pwd=" + URLEncoder.encode(pwd.getText().toString(), "UTF-8")
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
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
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
                btn.setBackgroundColor(getResources().getColor(R.color.gray_text));
			} else {
                btn.setBackgroundColor(getResources().getColor(R.color.blue));
			}
		}
	};

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public void onBackPressed() {
    }

    private class UpdateAllReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    final BroadcastReceiver updateAllReceiver = new UpdateAllReceiver();
}
