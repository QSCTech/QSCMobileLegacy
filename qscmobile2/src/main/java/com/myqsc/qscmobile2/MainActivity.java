package com.myqsc.qscmobile2;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.fragment.AboutUsActivity;
import com.myqsc.qscmobile2.fragment.CardFragment;
import com.myqsc.qscmobile2.fragment.MyFragmentPagerAdapter;
import com.myqsc.qscmobile2.fragment.cardlist.FunctionListFragment;
import com.myqsc.qscmobile2.login.LoginActivity;
import com.myqsc.qscmobile2.login.UserSwitchFragment;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.LogHelper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

	final List<Fragment> fragmentList = new ArrayList<Fragment>();
	MyFragmentPagerAdapter adapter = null;
	ViewPager vPager = null;
    int page = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);

		vPager = (ViewPager) findViewById(R.id.activity_main_viewpager);

		IntentFilter intentFilter = new IntentFilter(BroadcastHelper.BROADCAST_ONABOUTUS_CLICK);
		registerReceiver(new aboutusReceiver(), intentFilter);

        IntentFilter intentFilter2 = new IntentFilter(BroadcastHelper.BROADCAST_NEW_USER);
        registerReceiver(new newUserReceiver(), intentFilter2);
	}

    @Override
	protected void onResume() {
		super.onResume();
		
		final FunctionListFragment functionListFragment = new FunctionListFragment();
		
		final CardFragment cardFragment = new CardFragment();
		
		fragmentList.add(new UserSwitchFragment(this));
		fragmentList.add(functionListFragment);
		fragmentList.add(cardFragment);
		
		adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
		
		vPager.setAdapter(adapter);
        vPager.setCurrentItem(page);
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
	protected void onPause() {
		super.onPause();
        page = vPager.getCurrentItem();
		fragmentList.clear();
        adapter.notifyDataSetChanged();
	}
	
	private class aboutusReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(getApplicationContext(), AboutUsActivity.class);
            startActivity(intent1);
            overridePendingTransition(R.anim.right_push_in, 0);
		}
	}

    private class newUserReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_up_in, R.anim.fade_out);
        }
    }

}
