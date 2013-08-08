package com.myqsc.qscmobile2.login;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.PersonalDataHelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

public abstract class CheckUserAsyncTask extends AsyncTask<Void, Message, Message> implements HandleAsyncTaskMessage{
	final String url = "http://m.myqsc.com/dev/jw/validate?";
	String uid = null;
	String pwd = null;
	Context mContext = null;
	public CheckUserAsyncTask(String uid, String pwd, Context context){
		this.uid = uid;
		this.pwd = pwd;
		mContext = context;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		LogHelper.i("uid:" + uid);
		LogHelper.i("pwd:" + pwd);
	}



	@Override
	protected Message doInBackground(Void... params) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url + "stuid=" + uid + "&pwd=" + pwd);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			String result = EntityUtils.toString(httpResponse.getEntity());
			
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.optInt("code", 100) == 0){
				//用户名或密码错误
				Message msg = new Message();
				msg.what = 0;
				msg.obj = jsonObject.optString("msg");
				return msg;
			}
			
			if (jsonObject.optString("stuid", null) != null){
				//登陆成功，获取到了学号
				Message msg = new Message();
				msg.what = 1;
				return msg;
			}
			Message msg = new Message();
			msg.what = 0;
			msg.obj = "发生未知错误，登陆失败";
			return msg;
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = 0;
			msg.obj = "发生未知网络错误";
			return msg;
			
		} catch (IOException e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = 0;
			msg.obj = "网络读取错误";
			return msg;
			
		} catch (JSONException e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = 0;
			msg.obj = "数据解析失败，可能是网络不稳定";
			return msg;
			
		}
	}

	@Override
	protected void onPostExecute(Message result) {
		super.onPostExecute(result);
		if (result.what == 1){
			PersonalDataHelper helper = new PersonalDataHelper(mContext);
			helper.addUser(uid, pwd, true);
		}
		onHandleMessage(result);
	}
	

}
