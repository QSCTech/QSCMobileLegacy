package com.myqsc.qscmobile2.curriculum.uti;

import android.content.Context;
import android.os.Message;

import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.uti.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoDataHelper {
    HandleAsyncTaskMessage handleAsyncTaskMessage = null;
    Context mContext = null;

    public KebiaoDataHelper(Context context){
        this.mContext = context;
    }

    public void setHandleAsyncTaskMessage(HandleAsyncTaskMessage message){
        handleAsyncTaskMessage = message;
    }

    public void clear(){
        mContext
                .getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .remove(KebiaoClassData.PREFERENCE)
                .commit();
    }

    public void set(String result){
        clear();
        mContext
                .getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .putString(KebiaoClassData.PREFERENCE, result)
                .commit();
    }

    public void getDay (){

    }
}
