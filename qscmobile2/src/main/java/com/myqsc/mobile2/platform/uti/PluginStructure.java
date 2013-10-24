package com.myqsc.mobile2.platform.uti;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.handmark.pulltorefresh.library.R;
import com.myqsc.mobile2.platform.update.PlatformUpdateHelper;
import com.myqsc.mobile2.uti.LogHelper;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by richard on 13-9-9.
 */
public class PluginStructure implements Serializable {
    public String id, name, description;
    public String version;
    public Map<String, String> developer;
    public JSONObject permissions;
    public String [] web_accessible_resources;
    public String path;

    public PluginStructure() {

    }

    public PluginStructure(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        name = jsonObject.getString("name");
        description = jsonObject.getString("description");
        developer = new HashMap<String, String>();
        JSONObject temp = jsonObject.getJSONObject("developer");
        Iterator iterator = temp.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = temp.getString(key);
            developer.put(key, value);
        }

        permissions = jsonObject.getJSONObject("permissions");

        JSONArray jsonArray = jsonObject.getJSONArray("web_accessible_resources");

        web_accessible_resources = new String[jsonArray.length()];
        for(int i = 0; i != jsonArray.length(); ++i) {
            web_accessible_resources[i] = jsonArray.getString(i);
        }
        path = jsonObject.getString("path");
    }

    public void downloadPlugin(final Handler handler, final Context context) {
        final String base_url = PlatformUpdateHelper.URLBASE;
        final String url = base_url + path + '/';
        final int UPDATE_FINISH = 0x123;

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String file_url : web_accessible_resources) {
                    try {
                        File file = new File(context.getFilesDir(), PlatformUpdateHelper.PATH_ADD
                                + path + '/' + file_url);
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
                }

                Message message = handler.obtainMessage();
                message.arg1 = UPDATE_FINISH;
                message.obj = name + " 插件更新完成";
                message.sendToTarget();

                LogHelper.e((String) message.obj);
            }
        });

        thread.start();
    }
}
