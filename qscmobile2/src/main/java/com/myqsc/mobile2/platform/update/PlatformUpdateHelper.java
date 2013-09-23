package com.myqsc.mobile2.platform.update;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.myqsc.mobile2.platform.platform;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 13-9-8.
 */
public class PlatformUpdateHelper {
    final static String URLBASE = "http://qsctech.github.io/qsc-mobile-plugins/";
    final static String PATH_ADD = "platform/";

    final static String PLUGIN_LIST_PRE = "PLUGIN_LIST";
    final static String PLUGIN_PREFIX = "PLUGIN_PREFIX";

    public static void updatePlatform(final Context context, final Handler handler) {
        final Message message = handler.obtainMessage();
        message.what = 0;
        message.obj = "平台更新完成";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://qsctech.github.io/qsc-mobile-plugins/resources.json";
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    LogHelper.d("platform file downloaded");

                    String result = EntityUtils.toString(response.getEntity());
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i != jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        String path = jsonObject.getString("path");
                        JSONArray files = jsonObject.getJSONArray("web_accessible_resources");
                        LogHelper.d("platform json " + path + " download started");
                        for (int j = 0; j != files.length(); ++j) {
                            LogHelper.d("platform file " + files.getString(j) + " download start");
                            String file_url = URLBASE + path + files.getString(j);
                            File file = new File(context.getFilesDir(), PATH_ADD + path + "/" + files.getString(j));
                            if (file.exists())
                                file.delete();
                            file.getParentFile().mkdirs();
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

    public static void getPluginList(final Context context, final Handler handler) {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String URL = "http://qsctech.github.io/qsc-mobile-plugins/plugin-android.json";
                Message message = handler.obtainMessage();
                message.what = 0;
                message.obj = "插件列表下载完成";

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(URL);
                    HttpResponse httpResponse = httpClient.execute(httpGet);

                    String pluginList = EntityUtils.toString(httpResponse.getEntity());
                    if (pluginList != null)
                        context.getSharedPreferences(Utility.PREFERENCE, 0)
                                .edit()
                                .putString(PLUGIN_LIST_PRE, pluginList)
                                .commit();
                    message.what = 1;
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                message.sendToTarget();
            }
        });

        thread.start();
    }

    public static void updatePlugin(final String pluginID,
                                    final Context context,
                                    final Handler handler) {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//
//                try {
//                    HttpClient httpClient = new DefaultHttpClient();
//                    HttpGet httpGet = new HttpGet(URL);
//                    HttpResponse response = httpClient.execute(httpGet);
//                    String result = EntityUtils.toString(response.getEntity());
//                    JSONArray jsonArray = new JSONArray(result);
//
//                    for (int i = 0; i != jsonArray.length(); ++i) {
//                        JSONObject jsonObject = jsonArray.optJSONObject(i);
//                        context.getSharedPreferences(Utility.PREFERENCE, 0)
//                                .edit()
//                                .putString(platform.PLUGIN, jsonObject.toString())
//                                .commit();
//                        String id = jsonObject.getString("id");
//                        String path = jsonObject.getString("path");
//                        if (id.compareTo(pluginID) == 0) {
//                            JSONArray files = jsonObject.getJSONArray("web_accessible_resources");
//                            for (int j = 0; j != files.length(); ++j) {
//                                byte data[] = EntityUtils.toByteArray(
//                                        new DefaultHttpClient().execute(
//                                                new HttpGet(URLBASE + path + files.getString(j))).getEntity());
//                                File file = new File(context.getFilesDir(), PATH_ADD + path + "/" + files.getString(j));
//                                if (file.exists())
//                                    file.delete();
//                                file.getParentFile().mkdirs();
//                                file.createNewFile();
//
//                                FileOutputStream fileOutputStream = new FileOutputStream(file);
//                                fileOutputStream.write(data);
//                                fileOutputStream.close();
//                            }
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
