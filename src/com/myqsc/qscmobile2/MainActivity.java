package com.myqsc.qscmobile2;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.fragment.CardFragment;
import com.myqsc.qscmobile2.fragment.MyFragmentPagerAdapter;
import com.myqsc.qscmobile2.fragment.cardlist.FunctionListFragment;
import com.myqsc.qscmobile2.login.UserSwitchFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

	List<Fragment> fragmentList = new ArrayList<Fragment>();
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ViewPager vPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
		
		fragmentList.add(new UserSwitchFragment(this));
		fragmentList.add(new FunctionListFragment(this));
		fragmentList.add(new CardFragment());
		
		vPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
	}

	@Override
	protected void onStop() {
		super.onStop();
		fragmentList.clear();
	}
	
	

}
