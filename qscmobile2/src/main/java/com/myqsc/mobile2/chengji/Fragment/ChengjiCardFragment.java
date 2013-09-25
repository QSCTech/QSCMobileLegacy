package com.myqsc.mobile2.chengji.Fragment;

import com.myqsc.mobile2.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ChengjiCardFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.card_fragment_chengji, null);
		((TextView)view.findViewById(R.id.card_fragment_chengji_textLeft)).setText(Html.fromHtml("总学分 <font color='#007EF6'>118</font>"));
		((TextView)view.findViewById(R.id.card_fragment_chengji_textRight)).setText(Html.fromHtml("平均绩点 <font color='#007EF6'>4.15</font>"));
		
		return view;
	}

}
