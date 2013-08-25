package com.myqsc.qscmobile2.fragment;


import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.xiaoli.fragment.XiaoliCardFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class CardFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_card, null);
		
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_card_background, null);
		layout.setId(1);
		view.addView(layout);
		
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		XiaoliCardFragment fragment = new XiaoliCardFragment();
		transaction.add(R.id.fragment_card, fragment);
		transaction.commit();
		
		return view;
	}
}
