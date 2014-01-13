package com.myqsc.mobile2;

// TODO: Format tab into 4 spaces
// TODO: Format layout file
// TODO: Change pinyin into English words
// TODO: Change some of the class names to follow certain convention
// TODO: Make unnecessarily package-access members private

import java.io.IOException;
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
import com.myqsc.mobile2.uti.Utility;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

	final List<Fragment> fragmentList = new ArrayList<Fragment>();

	MyFragmentPagerAdapter pagerAdapter = null;
	ViewPager viewPager = null;
    int page = -1; //当前是第几个页面

    NewUserReceiver newUserReceiver = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        try {
            Runtime.getRuntime().exec("logcat -c");
        } catch (IOException e) {
            e.printStackTrace();
        }

        MobclickAgent.setDebugMode(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            UmengUpdateAgent.setUpdateOnlyWifi(false);
            UmengUpdateAgent.update(this);
        } else if (!getSharedPreferences(Utility.PREFERENCE, 0).contains("LegacyNotified")) {
            final Context legacyNotificationContext = this.getApplicationContext();
            (new AlertDialog.Builder(this)).setTitle(R.string.legacy_notify_title)
                    .setMessage(R.string.legacy_notify_content)
                    .setPositiveButton(R.string.legacy_notify_ok, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            legacyNotificationContext.getSharedPreferences(Utility.PREFERENCE, 0)
                                    .edit().putBoolean("LegacyNotified", true).commit();
                        }})
            .setCancelable(false)
            .create().show();
        }

        // TODO: Remove FrameLayout?
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
        // TODO: Change 5 to 2?
        viewPager.setOffscreenPageLimit(5);
        viewPager.setBackgroundDrawable(getResources().getDrawable(R.drawable.vpage_back));
        // TODO: Implement animation via ViewFlipper on Android 2.3?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        }

        newUserReceiver = new NewUserReceiver();

        final FunctionListFragment functionListFragment = new FunctionListFragment();
        final CardFragment cardFragment = new CardFragment();
        functionListFragment.addObserver(cardFragment);

        fragmentList.add(new UserSwitchFragment());
        fragmentList.add(functionListFragment);
        fragmentList.add(cardFragment);

        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogHelper.d("update all server started");
                startService(new Intent(MainActivity.this, UpdateAllService.class));
            }
        }, 4000);
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
            viewPager.setCurrentItem(page);
	}

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        page = viewPager.getCurrentItem();
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
