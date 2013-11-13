package com.myqsc.mobile2.platform.update;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.myqsc.mobile2.platform.uti.PluginStructure;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by richard on 13-9-8.
 */
public class PlatformUpdateHelper {
    public final static String URLBASE = "http://qsctech.github.io/qsc-mobile-plugins/";
    public final static String PATH_ADD = "platform/";

    public final static String PLATFORM_PREFERENCE = "PLATFORM_PREFERENCE";

    public final static String PLUGIN_LIST_PRE = "PLUGIN_LIST";
    public final static String PLUGIN_PREFIX = "PLUGIN_PREFIX_";
    public final static String PLUGIN_LIST_RAW = "PLUGIN_LIST_RAW";
    public final static String PLUGIN_PREFIX_SELECT = "PLUGIN_PREFIX_SELECT_";

    public final static int SUCCESS = 0, FAILED_LIST = 1, FAILED_DEWNLOAD = 2;

    /**
     * 以同步的方式更新平台文件列表
     * @return null if failed
     */
    private static String getPlatformResourceList() {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet("http://qsctech.github.io/qsc-mobile-plugins/resources.json");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以同步方式获取插件列表
     * @return
     */
    public static String syncGetPluginList () {
        final String URL = "http://qsctech.github.io/qsc-mobile-plugins/plugins.json";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以同步方式同步平台文件
     * @param context not null
     * @return boolean 表示成功或失败
     */
    public static boolean syncPlatformFile (final Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(PLATFORM_PREFERENCE, 0);

            String platformFileList = getPlatformResourceList();
            JSONArray jsonArray = new JSONArray(platformFileList);

            for (int i = 0; i != jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String path = jsonObject.getString("path");
                String filePathPrefix = PATH_ADD + path + '/';

                JSONArray web_accessible_resources = jsonObject.getJSONArray("web_accessible_resources");

                final boolean force = preferences.getString(filePathPrefix, "")
                        .equalsIgnoreCase(jsonObject.getString("version"));

                for (int j = 0; j != web_accessible_resources.length(); ++j) {
                    String fileName = web_accessible_resources.getString(j);
                    Utility.downloadFile(
                            URLBASE + path + '/' + fileName,
                            new File(context.getFilesDir(), filePathPrefix + fileName),
                            force
                    );
                }
                preferences.edit()
                        .putString(filePathPrefix, jsonObject.getString("version"))
                        .commit();
            }
            return true;

        } catch (Exception e) {
            //各种失败
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将插件列表字符串转换成list
     * @param string
     * @return
     */
    public static List<PluginStructure> parsePluginList (String string) {
        final List<PluginStructure> list = new ArrayList<PluginStructure>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            for (int i = 0; i != jsonArray.length(); ++i) {
                try {
                    //这里做一次异常处理，防止某个插件的问题导致全部列表失败
                    PluginStructure structure = new PluginStructure(
                            jsonArray.getJSONObject(i)
                    );
                    list.add(structure);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
