package com.myqsc.mobile2.homework;

import android.content.SharedPreferences;

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



    public HomeworkStructure(String id) {
        this.id = id;
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
        try {
            return preferences.getBoolean(SELECT_PREFIX + id, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改这个作业的选中状态，并且返回
     * @param preferences
     * @return
     */
    public boolean toggleSelect (SharedPreferences preferences) {
        boolean select = preferences.getBoolean(SELECT_PREFIX + id, false);
        preferences.edit()
                .putBoolean(SELECT_PREFIX + id, !select)
                .commit();
        return !select;
    }
}
