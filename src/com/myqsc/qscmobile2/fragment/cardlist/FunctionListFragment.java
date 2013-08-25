package com.myqsc.qscmobile2.fragment.cardlist;


import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.fragment.uti.OnFragmentMessage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FunctionListFragment extends Fragment {
	OnFragmentMessage onFragmentMessage = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cardlist, null);
		
		FunctionListAdapter adapter = new FunctionListAdapter(getActivity());
		final Handler adapterHandler = adapter.getHandler();
		((ListView)view.findViewById(R.id.fragment_cardlist)).setAdapter(adapter);
		((ListView)view.findViewById(R.id.fragment_cardlist)).setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Message message = new Message();
				message.what = 0;
				message.obj = arg1;
				message.arg1 = arg2;
				message.setTarget(adapterHandler);
				message.sendToTarget();
				
				if (onFragmentMessage != null){
					onFragmentMessage.onFragmentMessage(message);
				}
			}
		});
		return view;
	}
	
	public void setOnFragmentMessage(OnFragmentMessage message){
		this.onFragmentMessage = message;
	}
	
}
