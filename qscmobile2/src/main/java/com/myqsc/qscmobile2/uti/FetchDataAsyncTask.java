package com.myqsc.qscmobile2.uti;

import android.os.AsyncTask;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by richard on 13-8-31.
 */
public abstract class FetchDataAsyncTask extends AsyncTask<Void, Message, Message> {
    String url = null;

    public FetchDataAsyncTask(String url){
        this.url = url;
    }

    @Override
    protected Message doInBackground(Void... voids) {
        Message message = new Message();
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String result = EntityUtils.toString(httpResponse.getEntity());

            message.obj = result;
            message.what = 1;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            message.what = 0;
            message.obj = "网络初始化错误，请连接后重试";
        } catch (IOException e) {
            e.printStackTrace();
            message.what = 0;
            message.obj = "网络失败，请网络稳定后再试";
        }
        return message;
    }
}
