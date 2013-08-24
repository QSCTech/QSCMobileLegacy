package com.myqsc.qscmobile2.fragment.cardlist;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CardAdapter extends BaseAdapter {
	List<Fragment> list = new ArrayList<Fragment>();
	LayoutInflater mInflater = null;
	FragmentActivity activity = null;
	public CardAdapter(FragmentActivity context, List<Fragment> list){
		this.activity = context;
		mInflater = mInflater.from(activity);
		this.list = list;
	}
	
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = mInflater.inflate(R.layout.fragment_card_background, null);
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(convertView.getId(), list.get(position));
		transaction.commit();
		return convertView;
	}

}
