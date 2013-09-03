package com.myqsc.qscmobile2.login;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.login.uti.AboutListAdapter;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.login.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.uti.Utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

@SuppressLint("ValidFragment")
public class UserSwitchFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_switch, null);
		
		PersonalDataHelper personalDataHelper = new PersonalDataHelper(getActivity());
        List<UserIDStructure> allUserList = personalDataHelper.allUser();
        if (allUserList == null || allUserList.size() == 0){
            Intent intent = new Intent(BroadcastHelper.BROADCAST_NEW_USER);
            getActivity().sendBroadcast(intent);
        }
		return view;
	}

}
