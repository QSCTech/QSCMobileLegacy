package com.myqsc.qscmobile2.curriculum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.myqsc.qscmobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;

/**
 * 获取课程信息. 抽象类，需要重写 handleAsyncTaskMessage 接口
 * 
 * @see CourseData
 * @author LL
 */
public abstract class FetchCurriculum extends AsyncTask<Integer, Integer, Message>
        implements HandleAsyncTaskMessage{

	final static public int FROM_WEB = 1;
	
	UserIDStructure user;
    Context mContext = null;
	
	public FetchCurriculum(UserIDStructure user, Context context) {
		this.user = user;
        this.mContext = context;
	}
	
	@Override
	protected Message doInBackground(Integer... methods) {
		if(methods.length==0) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = "未指定获取方式";
			return msg;
		}
        return fetchFromWeb();
	}

    @Override
    protected void onPostExecute(Message message) {
        super.onPostExecute(message);
        onHandleMessage(message);
    }

    private Message fake() {
		Message msg = new Message();
		String result = "[{    \"id\": \"211G0060\",    \"name\": \"大学计算机基础\",    \"teacher\": \"陈建海\",    \"semester\": \"秋冬\",    \"hash\": \"15c56f3c10af0ab6f3c20a519886805e\",    \"class\": [	{	    \"week\": \"both\",	    \"weekday\": \"3\",	    \"place\": \"紫金港东1A-301(多)\",	    \"class\": [		\"1\",		\"2\"	    ]	},	{	    \"week\": \"odd\",	    \"weekday\": \"4\",	    \"place\": \"紫金港机房\",	    \"class\": [		\"3\",		\"4\"	    ]	}    ]},{    \"id\": \"371E0010\",    \"name\": \"形势与政策Ⅰ\",    \"teacher\": \"赵朝霞\",    \"semester\": \"秋冬\",    \"hash\": \"fb5e04f179cfabe555ad90ba4128d034\",    \"class\": [	{	    \"week\": \"odd\",	    \"weekday\": \"7\",	   \"place\": \"紫金港东1B-306(多)\",	    \"class\": [		\"11\",		\"12\"	    ]	}]}]";
		msg.what = 0;
		try {
			msg.obj = convert(result);
		} catch (JSONException e) {
			e.printStackTrace();
			msg.what = 1;
			msg.obj = "OTL";
		}
		return msg;
	}
	
	private Message fetchFromWeb() {
		Message msg = new Message();
		msg.what = 0;
		String url = "http://m.myqsc.com/dev/jw/kebiao?" + 
				"stuid=" + user.uid + 
				"&pwd=" + user.pwd;
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				msg.what = 1;
				msg.obj = "网络错误";
				return msg;
			}
			String result = EntityUtils.toString(httpResponse.getEntity());
			
			msg.obj = convert(result);

            KebiaoDataHelper helper = new KebiaoDataHelper(mContext);
            helper.set(result);

		} catch (ClientProtocolException e) {
			msg.what = 1;
			msg.obj = "网络初始化失败，请确定网络已经连接";
			e.printStackTrace();
		} catch (IOException e) {
			msg.what = 1;
			msg.obj = "网络错误";
			e.printStackTrace();
		} catch (JSONException e) {
			msg.what = 1;
			msg.obj = "数据解析失败，请在网络稳定后重试";
			e.printStackTrace();
		}
		return msg;
	}
	
	private List<CourseData> convert(String jsonString) throws JSONException {
		List<CourseData> list = new ArrayList<CourseData>();
		JSONArray courseArray = new JSONArray(jsonString);
		for (int i = 0; i < courseArray.length(); ++i) {
			JSONObject courseObject = courseArray.getJSONObject(i);
			CourseData course = new CourseData(courseObject);
			list.add(course);
		}
		return list;
	}

}
