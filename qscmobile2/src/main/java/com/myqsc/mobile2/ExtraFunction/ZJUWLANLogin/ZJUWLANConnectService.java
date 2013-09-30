package com.myqsc.mobile2.ExtraFunction.ZJUWLANLogin;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 13-9-29.
 */
public class ZJUWLANConnectService extends IntentService {
    public ZJUWLANConnectService() {
        super("ZJUWLANConnectService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences preferences = getSharedPreferences(Utility.PREFERENCE, 0);
        String username = preferences.getString(ZJUWLANActivity.PREFERENCE_STUID, "");
        String pwd      = preferences.getString(ZJUWLANActivity.PREFERENCE_PWD, "");
        long last       = preferences.getLong(ZJUWLANActivity.PREFERENCE_LAST, 100L);

        if (System.currentTimeMillis() - last <= 10000L) {
            return ;
        }

        if (username.length() < 1 || pwd.length() < 1)
            return ;

        try {
            login(preferences, username, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login(SharedPreferences preferences, String username, String pwd) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://net.zju.edu.cn/rad_online.php");
        //使用NameValuePair来保存要传递的Post参数
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        //添加要传递的参数
        postParameters.add(new BasicNameValuePair("action", "auto_dm"));
        postParameters.add(new BasicNameValuePair("uid", "-1"));
        postParameters.add(new BasicNameValuePair("username", username));
        postParameters.add(new BasicNameValuePair("password", pwd));

        //实例化UrlEncodedFormEntity对象
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                postParameters);

        //使用HttpPost对象来设置UrlEncodedFormEntity的Entity
        post.setEntity(formEntity);
        HttpResponse response = httpClient.execute(post);
        String res = EntityUtils.toString(response.getEntity());

        LogHelper.d(res);

        if ("ok".compareToIgnoreCase(res) != -1) {

            post = new HttpPost("http://net.zju.edu.cn/cgi-bin/srun_portal");
            httpClient = new DefaultHttpClient();

            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("action", "login"));
            postParameters.add(new BasicNameValuePair("username", username));
            postParameters.add(new BasicNameValuePair("password", pwd));
            postParameters.add(new BasicNameValuePair("ac_id", "5"));
            postParameters.add(new BasicNameValuePair("is_ldap", "1"));
            postParameters.add(new BasicNameValuePair("type", "2"));
            postParameters.add(new BasicNameValuePair("local_auth", "1"));
            formEntity = new UrlEncodedFormEntity(
                    postParameters);
            post.setEntity(formEntity);

            response = httpClient.execute(post);
            res = EntityUtils.toString(response.getEntity());

            if (res.contains("action=login_ok")) {
                LogHelper.d("ZJUWLAN LOGIN SUCCESSFUL");
                doToast("求是潮手机站：ZJUWLAN 登录成功");
                preferences.edit()
                        .putLong(ZJUWLANActivity.PREFERENCE_LAST, System.currentTimeMillis())
                        .commit();
            } else {
                if ("password_error".equalsIgnoreCase(res)) {
                    doToast("求是潮手机站：ZJUWLAN 密码错误");
                } else {
                    if ("username_error".equalsIgnoreCase(res)) {
                        doToast("求是潮手机站：ZJUWLAN 用户名错误");
                    } else {
                        doToast( "求是潮手机站：ZJUWLAN " + res);
                    }
                }
            }
        } else {
            doToast("求是潮手机站：ZJUWLAN " + res);
        }
    }

    Handler handler = new Handler();
    private void doToast(final String string) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ZJUWLANConnectService.this, string, Toast.LENGTH_LONG).show();
            }
        });
    }
}
