package com.myqsc.qscmobile2.fragment;


import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

@SuppressLint("ValidFragment")
public class CardFragment extends Fragment {
	List<String> list = null;
	public CardFragment(){
		this.list = new ArrayList<String>();
	}
	public CardFragment(List<String> list){
		this.list = list;
	}
	
	public CardFragment newInstance(List<String> list){
		return new CardFragment(list);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ScrollView view = (ScrollView) inflater.inflate(R.layout.fragment_card, null);
		LinearLayout baseLayout = (LinearLayout) view.findViewById(R.id.fragment_card_layout);
		
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		for(int i = 0; i != list.size(); ++i) {
			LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_card_background, null);
			layout.findViewById(R.id.fragment_card).setId(i);
			baseLayout.addView(layout);
			transaction.add(i, FragmentUtility.getCardFragmentByName(list.get(i)));
		}
		transaction.commit();
		return view;
	}
}
