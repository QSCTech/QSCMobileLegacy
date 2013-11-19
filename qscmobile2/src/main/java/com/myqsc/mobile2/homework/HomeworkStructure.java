package com.myqsc.mobile2.homework;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;

import org.json.JSONObject;

/**
 * Created by richard on 13-11-19.
 */
public class HomeworkStructure {
    private static final String SELECT_PREFIX = "HOMEWORK_SELECT_PREFIX";

    String id, hash, content;
    String due_time;
    long assign_time;
    String stuid;



    public HomeworkStructure() {
    }

    public HomeworkStructure (JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            hash = jsonObject.getString("courseHash");
            content = jsonObject.getString("content");
            due_time = jsonObject.getString("due_time");
            assign_time = jsonObject.getLong("assign_time");
            stuid = jsonObject.getString("stuid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSelected (SharedPreferences preferences) {
        return preferences.getBoolean(SELECT_PREFIX + id, false);
    }
}
