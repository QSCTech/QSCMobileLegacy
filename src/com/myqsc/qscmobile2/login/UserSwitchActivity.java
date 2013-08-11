package com.myqsc.qscmobile2.login;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.login.uti.UserSelectionAdapter;
import com.myqsc.qscmobile2.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.uti.Utility;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class UserSwitchActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_switch);
		ListView userListView = (ListView) findViewById(R.id.user_switch_user_list);
		
		PersonalDataHelper personalDataHelper = new PersonalDataHelper(this);
		UserSelectionAdapter userAdapter = new UserSelectionAdapter(getBaseContext(), personalDataHelper.allUser());
		
		userListView.setAdapter(userAdapter);
		Utility.setListViewHeightBasedOnChildren(userListView);
	}

}
