package com.myqsc.qscmobile2.exam.uti;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

public abstract class UpdateExamAsyncTask extends AsyncTask<Void, Message, Message> implements HandleAsyncTaskMessage {
	final String url = "http://m.myqsc.com/dev/jw/chengji?";
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
			
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.optInt("code", 100) == 0){
				//用户名或密码错误
				Message msg = new Message();
				msg.what = 0;
				msg.obj = jsonObject.optString("msg");
				return msg;
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
