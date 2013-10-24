package com.myqsc.mobile2.platform.uti;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.handmark.pulltorefresh.library.R;
import com.myqsc.mobile2.platform.update.PlatformUpdateHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by richard on 13-9-9.
 */
public class PluginStructure implements Serializable {
    public String id, name, description;
    public String version;
    public JSONObject developer;
    public JSONObject permissions;
    public String[] web_accessible_resources;
    public String path;

    public static final int UPDATE_FINISH = 0x123;

    public PluginStructure() {

    }

    public boolean isDownloaded(Context mContext) {
        String info = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(PlatformUpdateHelper.PLUGIN_PREFIX + id, null);

        if (info == null)
            return false;

        try {
            PluginStructure pluginStructure = new PluginStructure(new JSONObject(info));
            if (!pluginStructure.version.equals(version))
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        for (String file_url : web_accessible_resources) {
            File file = new File(
                    mContext.getFilesDir(),
                    PlatformUpdateHelper.PATH_ADD + path + '/' + file_url
            );
            if (!file.exists())
                return false;
        }
        return true;
    }

    public PluginStructure(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        name = jsonObject.getString("name");
        description = jsonObject.getString("description");
        version = jsonObject.getString("version");
        developer = jsonObject.getJSONObject("developer");

        permissions = jsonObject.getJSONObject("permissions");

        JSONArray jsonArray = jsonObject.getJSONArray("web_accessible_resources");

        web_accessible_resources = new String[jsonArray.length()];
        for (int i = 0; i != jsonArray.length(); ++i) {
            web_accessible_resources[i] = jsonArray.getString(i);
        }
        path = jsonObject.getString("path");
    }

    public JSONObject toJSONObject () {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("name", name);
            jsonObject.put("description", description);
            jsonObject.put("version", version);
            jsonObject.put("developer", developer);
            jsonObject.put("permissions", permissions);
            jsonObject.put("web_accessible_resources", new JSONArray(Arrays.asList(web_accessible_resources)));
            jsonObject.put("path", path);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public void downloadPlugin(final ProgressDialog progressDialog, final Context mContext) {
        final String base_url = PlatformUpdateHelper.URLBASE;
        final String url = base_url + path + '/';

        progressDialog.setMax(web_accessible_resources.length);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();


        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String file_url : web_accessible_resources) {
                    try {
                        File file = new File(mContext.getFilesDir(),
                                PlatformUpdateHelper.PATH_ADD + path + '/' + file_url);
                        if (file.exists())
                            file.delete();

                        file.getParentFile().mkdirs();
                        file.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);

                        byte data[] = EntityUtils.toByteArray(
                                new DefaultHttpClient().execute(
                                        new HttpGet(base_url + path + file_url)
                                ).getEntity()
                        );
                        fileOutputStream.write(data);
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //更新进度条
                            progressDialog.incrementProgressBy(1);
                        }
                    });
                }

                mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                        .edit()
                        .putString(PlatformUpdateHelper.PLUGIN_PREFIX + id, toJSONObject().toString())
                        .commit();


                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });

            }
        });

        thread.start();
    }
}
