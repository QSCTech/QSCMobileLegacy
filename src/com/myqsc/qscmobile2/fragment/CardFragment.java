package com.myqsc.qscmobile2.fragment;


import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.exam.fragment.ExamCardFragment;
import com.myqsc.qscmobile2.huodong.fragment.HuodongCardFragment;
import com.myqsc.qscmobile2.kebiao.fragment.KebiaoCardFragment;
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
import android.widget.ScrollView;

public class CardFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ScrollView view = (ScrollView) inflater.inflate(R.layout.fragment_card, null);
		LinearLayout baseLayout = (LinearLayout) view.findViewById(R.id.fragment_card_layout);
		
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_card_background, null);
		layout.findViewById(R.id.fragment_card).setId(1);
		baseLayout.addView(layout);
		
		layout = (LinearLayout) inflater.inflate(R.layout.fragment_card_background, null);
		layout.findViewById(R.id.fragment_card).setId(2);
		baseLayout.addView(layout);
		
		layout = (LinearLayout) inflater.inflate(R.layout.fragment_card_background, null);
		layout.findViewById(R.id.fragment_card).setId(3);
		baseLayout.addView(layout);
		
		layout = (LinearLayout) inflater.inflate(R.layout.fragment_card_background, null);
		layout.findViewById(R.id.fragment_card).setId(4);
		baseLayout.addView(layout);
		
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		transaction.add(1, new XiaoliCardFragment());
		transaction.add(2, new HuodongCardFragment());
		transaction.add(3, new KebiaoCardFragment());
		transaction.add(4, new ExamCardFragment());
		transaction.commit();
		
		return view;
	}
}
