package com.myqsc.qscmobile2.xiaoche;

import java.util.List;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.xiaoche.uti.XiaocheAdapter;
import com.myqsc.qscmobile2.xiaoche.uti.XiaocheDataHelper;
import com.myqsc.qscmobile2.xiaoche.uti.XiaocheStructure;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class XiaocheActivity extends Activity{
	ListView listView = null;
	Context mContext = null;

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiaoche);
		mContext = this;
		AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.activity_xiaoche_exchange), this);
		
		listView = (ListView) findViewById(R.id.activity_xiaoche_listview);
		
		XiaocheDataHelper helper = new XiaocheDataHelper(mContext);
		helper.setHandleAsyncTaskMessage(handleAsyncTaskMessage);
		helper.getBus("紫金港", "玉泉");
	}
	
	final HandleAsyncTaskMessage handleAsyncTaskMessage = new HandleAsyncTaskMessage() {
		
		@Override
		public void onHandleMessage(Message message) {
			LogHelper.d(message.toString());
			if (message.what == 0){
				Toast.makeText(getBaseContext(), (CharSequence) message.obj, Toast.LENGTH_LONG).show();
				return ;
			}
			XiaocheAdapter adapter = new XiaocheAdapter((List<XiaocheStructure>) message.obj, mContext, listView);
			listView.setAdapter(adapter);
		}
	}; 
	
}
