package com.myqsc.qscmobile2.login;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.login.uti.AboutListAdapter;
import com.myqsc.qscmobile2.login.uti.UserSelectionAdapter;
import com.myqsc.qscmobile2.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.uti.Utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class UserSwitchFragment extends Fragment{
	Context context = null;
	public UserSwitchFragment(Context context){
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_switch, null);
		
		ListView userListView = (ListView) view.findViewById(R.id.user_switch_user_list);
		PersonalDataHelper personalDataHelper = new PersonalDataHelper(context);
		UserSelectionAdapter userAdapter = new UserSelectionAdapter(getActivity(), personalDataHelper.allUser());
		userListView.setAdapter(userAdapter);
		Utility.setListViewHeightBasedOnChildren(userListView);
		
		ListView moreListView = (ListView) view.findViewById(R.id.user_switch_function_list);
		AboutListAdapter aboutListAdapter = new AboutListAdapter(getActivity());
		moreListView.setAdapter(aboutListAdapter);
		Utility.setListViewHeightBasedOnChildren(moreListView);
		
		return view;
	}

}
