package com.myqsc.qscmobile2.fragment.cardlist;

import com.myqsc.qscmobile2.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class FunctionListFragment extends Fragment {
	Context mContext = null;
	public FunctionListFragment(Context context){
		this.mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cardlist, null);
		((ListView)view.findViewById(R.id.fragment_cardlist)).setAdapter(new FunctionListAdapter(mContext));
		return view;
	}
	
}
