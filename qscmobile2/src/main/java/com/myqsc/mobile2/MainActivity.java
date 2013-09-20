package com.myqsc.mobile2;

import java.util.ArrayList;
import java.util.List;

import com.google.analytics.tracking.android.EasyTracker;
import com.myqsc.mobile2.fragment.CardFragment;
import com.myqsc.mobile2.fragment.MyFragmentPagerAdapter;
import com.myqsc.mobile2.fragment.cardlist.FunctionListFragment;
import com.myqsc.mobile2.login.LoginActivity;
import com.myqsc.mobile2.login.UserSwitchFragment;
import com.myqsc.mobile2.login.uti.PersonalDataHelper;
import com.myqsc.mobile2.support.database.structure.UserIDStructure;
import com.myqsc.mobile2.uti.BroadcastHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

	final List<Fragment> fragmentList = new ArrayList<Fragment>();
	MyFragmentPagerAdapter adapter = null;
	ViewPager vPager = null;

    NewUserReceiver newUserReceiver = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
        MobclickAgent.setDebugMode(true);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
		setContentView(R.layout.activity_main);

		vPager = (ViewPager) findViewById(R.id.activity_main_viewpager);

        newUserReceiver = new NewUserReceiver();

        fragmentList.add(new UserSwitchFragment());
        fragmentList.add(new FunctionListFragment());
        fragmentList.add(new CardFragment());

        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        vPager.setAdapter(adapter);
        vPager.setCurrentItem(2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    getThisProcessMemeryInfo();
                }
            }
        }).start();
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

        PersonalDataHelper personalDataHelper = new PersonalDataHelper(this);
        if (personalDataHelper.allUser() == null || personalDataHelper.allUser().size() == 0) {
            Intent intent = new Intent(BroadcastHelper.BROADCAST_NEW_USER);
            sendBroadcast(intent);
        }
    }

    @Override
	protected void onResume() {
		super.onResume();
        MobclickAgent.onResume(this);
        if (page != -1)
            vPager.setCurrentItem(page);
	}

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        page = vPager.getCurrentItem();
    }

    int page = -1; //当前是第几个页面
	
	public void getThisProcessMemeryInfo() {
        ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        int pid = android.os.Process.myPid();
        android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(new int[] {pid});
        LogHelper.i("内存使用：" + (int)memoryInfoArray[0].getTotalPrivateDirty() / 1024 + "mb");
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
