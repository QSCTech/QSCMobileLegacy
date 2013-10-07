package com.myqsc.mobile2;

// TODO: Format tab into 4 spaces
// TODO: Format layout file
// TODO: Change pinyin into English words
// TODO: Change some of the class names to follow certain convention
// TODO: Make unnecessarily package-access members private

import java.util.ArrayList;
import java.util.List;

import com.myqsc.mobile2.Service.UpdateAllService;
import com.myqsc.mobile2.fragment.CardFragment;
import com.myqsc.mobile2.fragment.MyFragmentPagerAdapter;
import com.myqsc.mobile2.fragment.ZoomOutPageTransformer;
import com.myqsc.mobile2.fragment.FunctionListFragment;
import com.myqsc.mobile2.login.LoginActivity;
import com.myqsc.mobile2.login.UserSwitchFragment;
import com.myqsc.mobile2.login.uti.PersonalDataHelper;
import com.myqsc.mobile2.uti.BroadcastHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

	final List<Fragment> fragmentList = new ArrayList<Fragment>();
    // TODO: Rename to pagerAdapter
	MyFragmentPagerAdapter adapter = null;
    // TODO: Rename to viewPager
	ViewPager vPager = null;

    NewUserReceiver newUserReceiver = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
        MobclickAgent.setDebugMode(true);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

        startService(new Intent(this, UpdateAllService.class));

        // TODO: Remove FrameLayout?
		setContentView(R.layout.activity_main);

		vPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
        // TODO: Change 5 to 2?
        vPager.setOffscreenPageLimit(5);
        vPager.setBackgroundDrawable(getResources().getDrawable(R.drawable.vpage_back));

        // TODO: Implement animation via ViewFlipper on Android 2.3?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            vPager.setPageTransformer(true, new ZoomOutPageTransformer());

        newUserReceiver = new NewUserReceiver();

        fragmentList.add(new UserSwitchFragment());
        fragmentList.add(new FunctionListFragment());
        fragmentList.add(new CardFragment());

        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        vPager.setAdapter(adapter);
        vPager.setCurrentItem(2);

//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    try{
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        break;
//                    }
//                    getThisProcessMemeryInfo();
//                }
//            }
//        });
//        thread.start();
    }

//    Thread thread = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        thread.interrupt();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(newUserReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // TODO: Rename intentFilter2
        // TODO: Use LocalBroadcastManager
        IntentFilter intentFilter2 = new IntentFilter(BroadcastHelper.BROADCAST_NEW_USER);
        registerReceiver(newUserReceiver, intentFilter2);

        PersonalDataHelper personalDataHelper = new PersonalDataHelper(this);
        // TODO: Should the condition be one of these two?
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

    // TODO: Change to getMemoryInfo()
	public void getThisProcessMemeryInfo() {
        ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        int pid = android.os.Process.myPid();
        android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(new int[] {pid});
        LogHelper.i("内存使用：" + (int)memoryInfoArray[0].getTotalPrivateDirty() / 1024 + "MB");
    }


    // TODO: Place this class before function definition?
    private class NewUserReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_up_in, R.anim.fade_out);
        }
    }
}
