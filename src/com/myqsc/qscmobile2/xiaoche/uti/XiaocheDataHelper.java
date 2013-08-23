package com.myqsc.qscmobile2.xiaoche.uti;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.Utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

public class XiaocheDataHelper {
	Context mContext = null;
	HandleAsyncTaskMessage handleAsyncTaskMessage = null;
	public void setHandleAsyncTaskMessage(HandleAsyncTaskMessage message){
		this.handleAsyncTaskMessage = handleAsyncTaskMessage;
	}
	
	public XiaocheDataHelper(Context context){
		this.mContext = context;
	}
	
	public void clear(){
		mContext.getSharedPreferences(Utility.PREFERENCE, 0).edit().remove(XiaocheStructure.PREFERENCE).commit();
	}
	
	public void update(String result){
		clear();
		mContext.getSharedPreferences(Utility.PREFERENCE, 0).edit().putString(XiaocheStructure.PREFERENCE, result).commit();
	}
	
	@SuppressLint("NewApi")
	public void getBus(final String start, final String stop) {
		String result = mContext.getSharedPreferences(Utility.PREFERENCE, 0).getString(XiaocheStructure.PREFERENCE, null);
		if (result != null){
			try {
				JSONArray jsonArray;
				jsonArray = new JSONArray(result);
				List<XiaocheStructure> list = new ArrayList<XiaocheStructure>();
				for(int i = 0; i != jsonArray.length(); ++i){
					JSONObject jsonObject = jsonArray.optJSONObject(i);
					XiaocheStructure structure = new XiaocheStructure(jsonObject);
					if (structure.startPos.equals(start) && structure.stopPos.equals(stop))
						list.add(structure);
				}
				Message message = new Message();
				message.what = 1;
				message.obj = list;
				if (handleAsyncTaskMessage != null)
					handleAsyncTaskMessage.onHandleMessage(message);
				return ;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		final UpdateXiaocheAsyncTask task = new UpdateXiaocheAsyncTask(mContext) {
			
			@Override
			public void onHandleMessage(Message message) {
				if (message.what == 1){
					getBus(start, stop);
					return ;
				}
				if (handleAsyncTaskMessage != null)
					handleAsyncTaskMessage.onHandleMessage(message);
			}
		};
		
		if (android.os.Build.VERSION.SDK_INT >= 11)
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			task.execute();
	}
}
