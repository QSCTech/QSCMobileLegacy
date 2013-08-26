package com.myqsc.qscmobile2;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.fragment.CardFragment;
import com.myqsc.qscmobile2.fragment.EmptyFragment;
import com.myqsc.qscmobile2.fragment.MyFragmentPagerAdapter;
import com.myqsc.qscmobile2.fragment.cardlist.FunctionListFragment;
import com.myqsc.qscmobile2.login.UserSwitchFragment;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.LogHelper;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MainActivity extends FragmentActivity {

	final List<Fragment> fragmentList = new ArrayList<Fragment>();
	MyFragmentPagerAdapter adapter = null;
	ViewPager vPager = null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		vPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
		
		IntentFilter intentFilter = new IntentFilter(BroadcastHelper.BROADCAST_ONABOUTUS_CLICK);
		registerReceiver(new aboutusReceiver(), intentFilter);
		
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		
		transaction.add(R.id.activity_main_frame, new EmptyFragment());
		transaction.commit();
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		final FunctionListFragment functionListFragment = new FunctionListFragment();
		
		final CardFragment cardFragment = new CardFragment();
		
		fragmentList.add(new UserSwitchFragment(this));
		fragmentList.add(functionListFragment);
		fragmentList.add(cardFragment);
		
		adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
		
		vPager.setAdapter(adapter);
		final Handler handler = new Handler();
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				getThisProcessMemeryInfo();
				handler.postDelayed(this, 1000);
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
	
	private class aboutusReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			
			AboutUsFragment fragment = new AboutUsFragment();
			transaction.replace(R.id.activity_main_frame, fragment);
			transaction.setCustomAnimations(R.anim.push_down_in, R.anim.push_down_out);
			findViewById(R.id.activity_main_frame).setVisibility(View.VISIBLE);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}
}
