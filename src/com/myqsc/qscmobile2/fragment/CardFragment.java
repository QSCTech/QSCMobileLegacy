package com.myqsc.qscmobile2.fragment;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.fragment.cardlist.CardAdapter;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.xiaoli.fragment.XiaoliCardFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CardFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_card, null);
		
		ListView listView = (ListView) view.findViewById(R.id.fragment_card_list);
		List<Fragment> list = new ArrayList<Fragment>();
		list.add(new XiaoliCardFragment());
		CardAdapter adapter = new CardAdapter(getActivity(), list);
		listView.setAdapter(adapter);
		
	
		return view;
	}
	
}
