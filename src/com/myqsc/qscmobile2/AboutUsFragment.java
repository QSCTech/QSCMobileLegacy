package com.myqsc.qscmobile2;

import com.myqsc.qscmobile2.uti.AwesomeFontHelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutUsFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about_us, null);
		AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.fragment_aboutus_icon), getActivity());
		return view;
	}

}
