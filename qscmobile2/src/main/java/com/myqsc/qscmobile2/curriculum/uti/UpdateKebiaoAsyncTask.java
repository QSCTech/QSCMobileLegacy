package com.myqsc.qscmobile2.curriculum.uti;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public abstract class UpdateKebiaoAsyncTask extends AsyncTask<Void, Message, Message>
        implements HandleAsyncTaskMessage{
    Context mContext = null;
    UserIDStructure structure = null;

    public UpdateKebiaoAsyncTask(Context context, UserIDStructure structure){
        this.mContext = context;
        this.structure = structure;
    }

    @Override
    protected Message doInBackground(Void... voids) {
        Message message = new Message();

        final String url = "http://m.myqsc.com/v2/jw/kebiao?"
                + "stuid=" + structure.uid
                + "&pwd=" + structure.pwd;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String result = EntityUtils.toString(httpResponse.getEntity());

            List<KebiaoClassData> list = KebiaoClassData.parse(new JSONArray(result));
            message.what = 0;
            message.obj = list;

            KebiaoDataHelper helper = new KebiaoDataHelper(mContext);
            helper.set(result);

        }catch (JSONException e){
            e.printStackTrace();
            message.what = 0;
            message.obj = "数据解析失败，请在网络稳定后再试";
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            message.what = 0;
            message.obj = "网络初始化失败，请连接网络后再试";
        } catch (IOException e) {
            e.printStackTrace();
            message.what = 0;
            message.obj = "网络读取失败，请稳定后再试";
        }
        return message;
    }
}
