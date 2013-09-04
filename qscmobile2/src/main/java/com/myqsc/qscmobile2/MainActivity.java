package com.myqsc.qscmobile2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.analytics.tracking.android.EasyTracker;
import com.myqsc.qscmobile2.fragment.CardFragment;
import com.myqsc.qscmobile2.fragment.MyFragmentPagerAdapter;
import com.myqsc.qscmobile2.fragment.cardlist.FunctionListFragment;
import com.myqsc.qscmobile2.login.LoginActivity;
import com.myqsc.qscmobile2.login.UserSwitchFragment;
import com.myqsc.qscmobile2.login.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.xiaoli.uti.XiaoliHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

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

    NewUserReceiver newUserReceiver = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
        MobclickAgent.setDebugMode(true);
        UmengUpdateAgent.update(this);
		setContentView(R.layout.activity_main);

		vPager = (ViewPager) findViewById(R.id.activity_main_viewpager);

        newUserReceiver = new NewUserReceiver();

        PersonalDataHelper personalDataHelper = new PersonalDataHelper(this);
        List<UserIDStructure> userIDStructureList = personalDataHelper.allUser();

        final CardFragment cardFragment = new CardFragment();

        fragmentList.add(new UserSwitchFragment());
        fragmentList.add(new FunctionListFragment());
        fragmentList.add(cardFragment);

        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        vPager.setAdapter(adapter);
        vPager.setCurrentItem(page);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
        unregisterReceiver(newUserReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);

        IntentFilter intentFilter2 = new IntentFilter(BroadcastHelper.BROADCAST_NEW_USER);
        registerReceiver(newUserReceiver, intentFilter2);
    }

    @Override
	protected void onResume() {
		super.onResume();
        MobclickAgent.onResume(this);


//		final Handler handler = new Handler();
//		handler.post(new Runnable() {
//
//			@Override
//			public void run() {
//				getThisProcessMemeryInfo();
//				handler.postDelayed(this, 1000);
//			}
//		});
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
        MobclickAgent.onPause(this);
	}

    private class NewUserReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_up_in, R.anim.fade_out);
        }
    }
}
