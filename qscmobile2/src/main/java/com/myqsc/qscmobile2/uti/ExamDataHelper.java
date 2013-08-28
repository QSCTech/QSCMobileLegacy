package com.myqsc.qscmobile2.uti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.myqsc.qscmobile2.exam.uti.UpdateExamAsyncTask;
import com.myqsc.qscmobile2.support.database.structure.ExamStructure;

@SuppressLint("NewApi")
public class ExamDataHelper {
	Context mContext = null;
	HandleAsyncTaskMessage handleAsyncTaskMessage = null;
	public ExamDataHelper(Context context) {
		this.mContext = context;
	}
	
	public void setHandleAsyncTaskMessage(HandleAsyncTaskMessage message){
		this.handleAsyncTaskMessage = message;
	}
	
	public void clear(){
		mContext.getSharedPreferences(Utility.PREFERENCE, 0).edit().remove(ExamStructure.PREFERENCE).commit();
	}
	
	public void update_exam(List<ExamStructure> list) throws JSONException{
		this.clear();
		final String term[] = {"春", "夏", "秋", "冬"};
		Map<String, List<ExamStructure>> term_list = new HashMap<String, List<ExamStructure>>();
		for (String string : term) {
			term_list.put(string, new ArrayList<ExamStructure>());
		}
		for (ExamStructure examStructure : list) {
			for (String string : term) {
				if (examStructure.term.indexOf(string) != -1){
					term_list.get(string).add(examStructure);
				}
			}
		}
		for (String string : term_list.keySet()) {
			list = term_list.get(string);
			JSONArray jsonArray = new JSONArray();
			for (ExamStructure examStructure : list) {
				jsonArray.put(examStructure.toJsonObject());
			}
			mContext.getSharedPreferences(Utility.PREFERENCE, 0).edit().putString(ExamStructure.PREFERENCE + string, jsonArray.toString()).commit();
		}
	}
	
	public void getExamList(final String term, final String uid, final String pwd){
		Message message = new Message();
		String result = mContext.getSharedPreferences(Utility.PREFERENCE, 0).getString(ExamStructure.PREFERENCE + term, null);
		if (result != null){
			try {
				List<ExamStructure> list = new ArrayList<ExamStructure>();
				JSONArray jsonArray = new JSONArray(result);
				for(int i = 0; i != jsonArray.length(); ++i){
					ExamStructure examStructure = new ExamStructure(jsonArray.optJSONObject(i));
					list.add(examStructure);
				}
				message.what = 1;
				message.obj = list;
				if (handleAsyncTaskMessage != null)
					handleAsyncTaskMessage.onHandleMessage(message);
				return ;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} 
		
		final UpdateExamAsyncTask task = new UpdateExamAsyncTask(mContext, uid, pwd) {
			
			@Override
			public void onHandleMessage(Message message) {
				LogHelper.d(message.toString());
				if (message.what == 0){
					if (handleAsyncTaskMessage != null)
						handleAsyncTaskMessage.onHandleMessage(message);
					return;
				}
				getExamList(term, uid, pwd);
			}
		};
		
		if (android.os.Build.VERSION.SDK_INT >= 11)
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			task.execute();
		
	}
}
