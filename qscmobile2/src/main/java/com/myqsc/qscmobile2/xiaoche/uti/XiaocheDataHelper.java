package com.myqsc.qscmobile2.xiaoche.uti;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.myqsc.qscmobile2.network.DataUpdater;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.Utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

public class XiaocheDataHelper {
	Context mContext = null;
	HandleAsyncTaskMessage handleAsyncTaskMessage = null;
	public void setHandleAsyncTaskMessage(HandleAsyncTaskMessage message){
		this.handleAsyncTaskMessage = message;
	}
	
	public XiaocheDataHelper(Context context){
		this.mContext = context;
	}
	
	public void clear(){
		mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .edit().remove(DataUpdater.COMMON_XIAOCHE).commit();
	}
	
	public void update(String result){
		clear();
		mContext.getSharedPreferences(Utility.PREFERENCE, 0).edit()
                .putString(DataUpdater.COMMON_XIAOCHE, result).commit();
	}
	
	public List<XiaocheStructure> getBus(final String start, final String stop) {
		String result = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(DataUpdater.COMMON_XIAOCHE, null);
        assert result != null;
        try {
            JSONArray jsonArray;
            jsonArray = new JSONArray(result);
            List<XiaocheStructure> list = new ArrayList<XiaocheStructure>();
            for(int i = 0; i != jsonArray.length(); ++i){
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                XiaocheStructure structure = new XiaocheStructure(jsonObject);
                if (structure.startPos.indexOf(start) != -1 && structure.stopPos.indexOf(stop) != -1){
                    list.add(structure);
                }
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
		}
        throw new RuntimeException("校车数据为空");
	}
}
