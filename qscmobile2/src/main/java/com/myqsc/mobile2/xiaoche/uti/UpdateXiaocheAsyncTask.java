package com.myqsc.mobile2.xiaoche.uti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.myqsc.mobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.mobile2.uti.LogHelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

public abstract class UpdateXiaocheAsyncTask extends AsyncTask<Void, Message, Message>
        implements HandleAsyncTaskMessage {
	final String url = "http://m.myqsc.com/dev/share/xiaoche";
	Context mContext = null;
	public UpdateXiaocheAsyncTask(Context context){
		mContext = context;
	}
	
	@Override
	protected Message doInBackground(Void... params) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			String result = EntityUtils.toString(httpResponse.getEntity());
			
			LogHelper.i(result);
			JSONTokener jsonTokener = new JSONTokener(result);
			Object next = jsonTokener.nextValue();
			JSONArray jsonArray = null;
			if (next instanceof JSONArray)
				jsonArray = (JSONArray) next;
			else {
				throw new Exception();
			}
			
			List<XiaocheStructure> list = new ArrayList<XiaocheStructure>();
			for(int i = 0; i != jsonArray.length(); ++i){
				JSONObject jsonObject = jsonArray.optJSONObject(i);
				XiaocheStructure structure = new XiaocheStructure(jsonObject);
				list.add(structure);
			}
			Message message = new Message();
			message.what = 1;
			message.obj = list;
			
			XiaocheDataHelper helper = new XiaocheDataHelper(mContext);
			helper.update(result);
			return message;
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Message message = new Message();
			message.what = 0;
			message.obj = "网络失败，请链接后重试";
			return message;
		} catch (IOException e) {
			e.printStackTrace();
			Message message = new Message();
			message.what = 0;
			message.obj = "网络失败，请重试";
			return message;
		} catch (JSONException e) {
			e.printStackTrace();
			Message message = new Message();
			message.what = 0;
			message.obj = "网络错误，数据解析失败，请稍后重试";
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			Message message = new Message();
			message.what = 0;
			message.obj = "发生未知错误，请稍后重试";
			return message;
		}
	}

	@Override
	protected void onPostExecute(Message result) {
		super.onPostExecute(result);
		onHandleMessage(result);
	}
}
