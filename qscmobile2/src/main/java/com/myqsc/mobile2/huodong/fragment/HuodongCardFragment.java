package com.myqsc.mobile2.huodong.fragment;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.AwesomeFontHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HuodongCardFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.card_fragment_huodong, null);
		
		AwesomeFontHelper.setFontFace((TextView)view.findViewById(R.id.card_huodong_icon1), getActivity());
		AwesomeFontHelper.setFontFace((TextView)view.findViewById(R.id.card_huodong_icon2), getActivity());
		AwesomeFontHelper.setFontFace((TextView)view.findViewById(R.id.card_huodong_icon3), getActivity());
		return view;
	}

}
