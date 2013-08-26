package com.myqsc.qscmobile2.fragment;

import java.util.List;

import com.myqsc.qscmobile2.uti.LogHelper;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
	List<Fragment> list;
	Handler handler = null;
	int changed = 0;
	public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
		handler = new Handler(callback);
	}
	
	public Handler getHandler() {
		return handler;
	}
	
	private final Handler.Callback callback = new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == 1){
				changed = 1;
				LogHelper.d("data changed");
				notifyDataSetChanged();
			}
			return true;
		}
	};

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
