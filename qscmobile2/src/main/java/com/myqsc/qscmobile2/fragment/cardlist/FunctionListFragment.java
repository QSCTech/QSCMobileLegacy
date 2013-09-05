package com.myqsc.qscmobile2.fragment.cardlist;


import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.LogHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FunctionListFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogHelper.d("FunctionListFragment OnCreateView called");
		View view = inflater.inflate(R.layout.fragment_cardlist, null);
		
		final FunctionListAdapter adapter = new FunctionListAdapter(getActivity());
		((ListView) view.findViewById(R.id.fragment_cardlist))
                .setAdapter(adapter);
		((ListView) view.findViewById(R.id.fragment_cardlist))
                .setOnItemClickListener(onItemClickListener);
		return view;
	}
	
	final OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(BroadcastHelper.BROADCAST_FUNCTIONLIST_ONITEMCLICKED);
			intent.putExtra("position", arg2);
			getActivity().sendBroadcast(intent);
		}
	};
}
