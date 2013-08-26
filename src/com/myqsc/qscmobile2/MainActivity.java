package com.myqsc.qscmobile2;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.fragment.CardFragment;
import com.myqsc.qscmobile2.fragment.MyFragmentPagerAdapter;
import com.myqsc.qscmobile2.fragment.cardlist.FunctionListFragment;
import com.myqsc.qscmobile2.fragment.uti.OnFragmentMessage;
import com.myqsc.qscmobile2.login.UserSwitchFragment;
import com.myqsc.qscmobile2.uti.LogHelper;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

	final List<Fragment> fragmentList = new ArrayList<Fragment>();
	MyFragmentPagerAdapter adapter = null;
	ViewPager vPager = null;
	Handler fragmentPagerAdapterHandler = null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		vPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
	}

	@Override
	protected void onStart() {
		super.onResume();
		
		final FunctionListFragment functionListFragment = new FunctionListFragment();
		functionListFragment.setOnFragmentMessage(functionListFragmentMessage);
		
		final CardFragment cardFragment = new CardFragment();
		
		fragmentList.add(new UserSwitchFragment(this));
		fragmentList.add(functionListFragment);
		fragmentList.add(cardFragment);
		
		adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
		fragmentPagerAdapterHandler = adapter.getHandler();
		
		vPager.setAdapter(adapter);
		
		final Handler handler = new Handler();
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				getThisProcessMemeryInfo();
				handler.postDelayed(this, 10000);
			}
		});
	}
	
	public void getThisProcessMemeryInfo() {
        ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        int pid = android.os.Process.myPid();
        android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(new int[] {pid});
        LogHelper.i("内存使用：" + (int)memoryInfoArray[0].getTotalPrivateDirty() / 1024 + "mb");
    }

	@Override
	protected void onStop() {
		super.onStop();
		fragmentList.clear();
	}
	
	private final OnFragmentMessage functionListFragmentMessage = new OnFragmentMessage() {
		
		@Override
		public void onFragmentMessage(Message message) {
			LogHelper.i(message.toString());
			if (message.what == 1){
				CardFragment fragment = new CardFragment((List<String>) message.obj);
				fragmentList.set(2, fragment);
				message.setTarget(fragmentPagerAdapterHandler);
				message.sendToTarget();
			}
		}
	};
	
	

}
