package com.myqsc.qscmobile2.exam.uti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.myqsc.qscmobile2.support.database.structure.ExamStructure;
import com.myqsc.qscmobile2.uti.ExamDataHelper;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.LogHelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

public abstract class UpdateExamAsyncTask extends AsyncTask<Void, Message, Message> implements HandleAsyncTaskMessage {
	final String url = "http://m.myqsc.com/dev/jw/kaoshi?";
	String uid = null;
	String pwd = null;
	Context mContext = null;
	public UpdateExamAsyncTask(Context context, String uid, String pwd){
		this.uid = uid;
		this.pwd = pwd;
		this.mContext = context;
	}
	
	@Override
	protected Message doInBackground(Void... params) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url 
					+ "stuid=" + uid
					+ "&pwd=" + pwd);
			HttpResponse response = httpClient.execute(httpGet);
			String result = EntityUtils.toString(response.getEntity());
			response = null;
			httpGet = null;
			httpClient = null;
			
			LogHelper.i(result);
			JSONTokener tokener = new JSONTokener(result);
			JSONArray jsonArray = (JSONArray) tokener.nextValue();
			List<ExamStructure> list = new ArrayList<ExamStructure>();
			
			for (int i = 0; i != jsonArray.length(); ++i){
				JSONObject jsonObject = jsonArray.optJSONObject(i);
				ExamStructure structure = new ExamStructure(jsonObject);
				list.add(structure);
			}
			Message msg = new Message();
			msg.what = 1;
			msg.obj = list;
			return msg;
			
		} catch (IOException e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = 0;
			msg.obj = "网络错误，请重试";
			return msg;
		} catch (JSONException e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = 0;
			msg.obj = "数据解析失败，可能是用户名或密码错误";
			return msg;
		}
	}

	@Override
	protected void onPostExecute(Message result) {
		super.onPostExecute(result);
		if (result.what == 1){
			ExamDataHelper helper = new ExamDataHelper(mContext);
			helper.clear();
			for (ExamStructure structure : (List<ExamStructure>) result.obj) {
				helper.add_exam(structure);
			}
		}
		onHandleMessage(result);
	}

}
