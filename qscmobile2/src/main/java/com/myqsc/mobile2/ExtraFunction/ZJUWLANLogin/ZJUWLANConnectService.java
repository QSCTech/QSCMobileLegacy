package com.myqsc.mobile2.ExtraFunction.ZJUWLANLogin;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.Toast;

import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
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
        LogHelper.e("zjuwlan login server started");
        SharedPreferences preferences = getSharedPreferences(Utility.PREFERENCE, 0);
        String username = preferences.getString(ZJUWLANActivity.PREFERENCE_STUID, "");
        String pwd      = preferences.getString(ZJUWLANActivity.PREFERENCE_PWD, "");
        LogHelper.e("zjuwlan login server started");
        LogHelper.e(username);
        LogHelper.e(pwd);

        if (username.length() < 1 || pwd.length() < 1)
            return ;

        MobclickAgent.onEvent(ZJUWLANConnectService.this, "ZJUWLAN LOGIN START");

        try {
            login(preferences, username, pwd);
            //登陆完成，开始数据收集
            push();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将现有数据上传
     */
    private void push () {
        try {
            SharedPreferences preferences = ZJUWLANConnectService.this
                    .getSharedPreferences(ZJUWLANConnectReceiver.PREFER, 0);

            String data = preferences.getString(ZJUWLANConnectReceiver.HISTORY, null);
            if (data != null) {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("https://m.myqsc.com/api/v2/wireless/add");

                List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("data", data));
                httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));


                HttpResponse httpResponse = httpClient.execute(httpPost);
                LogHelper.e(data);
                LogHelper.e(EntityUtils.toString(httpResponse.getEntity()));

                preferences.edit()
                        .putString(ZJUWLANConnectReceiver.HISTORY, null)
                        .commit();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login(SharedPreferences preferences, String username, String pwd) throws IOException {
        LogHelper.e("login finished");

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://net.zju.edu.cn/rad_online.php");
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

            post = new HttpPost("https://net.zju.edu.cn/cgi-bin/srun_portal");
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
                MobclickAgent.onEvent(ZJUWLANConnectService.this, "ZJUWLAN LOGIN SUCCESS");
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
            doToast("求是潮手机站：ZJUWLAN 未知错误");
        }
    }

    Handler handler = new Handler();
    private void doToast(final String string) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ZJUWLANConnectService.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
