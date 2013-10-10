package com.myqsc.mobile2.login;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.fragment.LoadFragment;
import com.myqsc.mobile2.login.uti.PersonalDataHelper;
import com.myqsc.mobile2.network.DataUpdater;
import com.myqsc.mobile2.support.database.structure.UserIDStructure;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
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

import java.io.IOException;
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
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        btn.setEnabled(false);

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

        final Handler handler = new Handler();

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
                transaction.commit();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = DataUpdater.get(
                                    DataUpdater.name.get(DataUpdater.JW_VALIDATE) +
                                        "?stuid=" + URLEncoder.encode(uid.getText().toString(), "UTF-8") +
                                        "&pwd=" + URLEncoder.encode(pwd.getText().toString(), "UTF-8")
                            );
                            LogHelper.d(result);

                            if (result == null){
                                throw new IOException("网络错误");
                            }

                            JSONObject jsonObject = new JSONObject(result);
                            jsonObject.getString("stuid");
                            //解析到了学号代表登陆成功

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    PersonalDataHelper helper = new PersonalDataHelper(activity);
                                    helper.add(
                                            new UserIDStructure(
                                                    uid.getText().toString(),
                                                    pwd.getText().toString()));
                                    finish();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "数据解析失败，可能是密码错误或教务网暂时不可用", Toast.LENGTH_LONG).show();
                                    if (manager.getBackStackEntryCount() != 0)
                                        manager.popBackStack();
                                    findViewById(R.id.login_acitivity_main_layout).setVisibility(View.VISIBLE);
                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "密码中含有过于特殊的字符，无法提交，请联系求是潮处理", Toast.LENGTH_LONG).show();
                                    if (manager.getBackStackEntryCount() != 0)
                                        manager.popBackStack();
                                    findViewById(R.id.login_acitivity_main_layout).setVisibility(View.VISIBLE);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "网络失败，请稳定后再试", Toast.LENGTH_LONG).show();
                                    if (manager.getBackStackEntryCount() != 0)
                                        manager.popBackStack();
                                    findViewById(R.id.login_acitivity_main_layout).setVisibility(View.VISIBLE);
                                }
                            });
                        }
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
                btn.setEnabled(false);
			} else {
                btn.setBackgroundColor(getResources().getColor(R.color.blue));
                btn.setEnabled(true);
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

}
