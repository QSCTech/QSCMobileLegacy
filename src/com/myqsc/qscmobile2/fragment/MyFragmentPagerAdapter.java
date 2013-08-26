package com.myqsc.qscmobile2.fragment;

import java.util.List;

import com.myqsc.qscmobile2.uti.LogHelper;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	List<Fragment> list;
	int changed = 0;
	public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		LogHelper.d("item" + position + "destroy");
	}

}
