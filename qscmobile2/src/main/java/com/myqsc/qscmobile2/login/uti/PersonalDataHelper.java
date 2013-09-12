package com.myqsc.qscmobile2.login.uti;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.support.database.table.UserIDTable;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;

public class PersonalDataHelper {
    Context context = null;
    List<UserIDStructure> list = new ArrayList<UserIDStructure>();

    public PersonalDataHelper(Context context){
        this.context = context;
        String result = context.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(UserIDStructure.PREFERENCE, null);
        if (result != null) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i != jsonArray.length(); ++i) {
                    list.add(new UserIDStructure(jsonArray.optJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public List<UserIDStructure> allUser() {
        return list;
    }

    public void save() {
        JSONArray jsonArray = new JSONArray();
        for(UserIDStructure structure : list) {
            jsonArray.put(structure.toJsonObject());
        }
        context.getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .putString(UserIDStructure.PREFERENCE, jsonArray.toString())
                .commit();
    }

    public void add(UserIDStructure userIDStructure) {
        for (int i = 0; i < list.size(); ++i)
            if (list.get(i).uid.compareTo(userIDStructure.uid) == 0)
                list.remove(i);
        list.add(userIDStructure);
        setDefault(userIDStructure.uid);
        save();
    }

    public void setDefault(String uid) {
        for(UserIDStructure structure : list) {
            if (structure.uid.compareTo(uid) == 0)
                structure.select = true;
            else
                structure.select = false;
        }
        save();
        context.sendBroadcast(new Intent(BroadcastHelper.BROADCAST_USER_CHANGED));
    }

    public UserIDStructure getCurrentUser() {
        for (UserIDStructure structure : list) {
            if (structure.select)
                return structure;
        }
        return null;
    }

    public int deleteDefault() {
        for (int i = 0; i < list.size(); ++i)
            if (list.get(i).select)
                list.remove(i);
        save();
        if (list.size() > 0) {
            setDefault(list.get(0).uid);
            save();
            return 0;
        } else
            return -1;
    }

}
