package com.myqsc.qscmobile2.platform.uti;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by richard on 13-9-8.
 */
public class PlatformUpdateHelper {
    public static void updatePlatform(final Context context, final Handler handler) {
        final Message message = handler.obtainMessage();
        message.what = 0;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String URLBASE = "http://qsctech.github.io/qsc-mobile-plugins/";
                final String url = "http://qsctech.github.io/qsc-mobile-plugins/resources.json";
                final String PATH_ADD = "platform/";
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    String result = EntityUtils.toString(response.getEntity());
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i != jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        String path = jsonObject.getString("path");
                        JSONArray files = jsonObject.getJSONArray("web_accessible_resources");
                        for (int j = 0; j != files.length(); ++j) {
                            String file_url = URLBASE + path + files.getString(j);
                            File file = new File(context.getFilesDir(), PATH_ADD + path + files.getString(j));
                            if (file.exists())
                                file.delete();
                            file.createNewFile();
                            FileOutputStream fileOutputStream = new FileOutputStream(file);

                            byte data[] = EntityUtils.toByteArray(
                                    new DefaultHttpClient().execute(
                                            new HttpGet(file_url)).getEntity());
                            fileOutputStream.write(data);
                            fileOutputStream.close();
                        }
                    }

                    message.what = 1;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                message.sendToTarget();
            }
        });

        thread.start();
    }
}
