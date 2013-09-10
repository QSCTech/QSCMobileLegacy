package com.myqsc.qscmobile2.platform.uti;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by richard on 13-9-9.
 */
public class PluginStructure {
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
}
