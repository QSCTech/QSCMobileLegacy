package com.myqsc.mobile2.ExtraFunction.ZJUWLANLogin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.Utility;
import com.umeng.analytics.MobclickAgent;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-9-28.
 */
public class ZJUWLANActivity extends SwipeBackActivity {
    public static String PREFERENCE_STUID = "ZJUWLAN_STUID";
    public static String PREFERENCE_PWD = "ZJUWLAN_PWD";
    public static String PREFERENCE_LAST = "PREFERENCE_LAST";

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    EditText editTextStuid, editTextPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zjuwlan_login);

        editTextStuid   = (EditText) findViewById(R.id.zju_login_stuid);
        editTextPwd     = (EditText) findViewById(R.id.zju_login_pwd);

        Button button = (Button) findViewById(R.id.zju_login_button);

        final SharedPreferences preferences = getSharedPreferences(Utility.PREFERENCE, 0);
        editTextStuid.setText(preferences.getString(PREFERENCE_STUID, ""));
        editTextPwd.setText(preferences.getString(PREFERENCE_PWD, ""));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString(PREFERENCE_STUID, editTextStuid.getEditableText().toString());
                editor.putString(PREFERENCE_PWD, editTextPwd.getEditableText().toString());

                editor.commit();

                Toast.makeText(getApplicationContext(), "保存成功，将在连接ZJUWLAN后自动登录哦~", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
        });

    }
}
