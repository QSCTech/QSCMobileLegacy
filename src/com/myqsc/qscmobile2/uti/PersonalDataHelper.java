package com.myqsc.qscmobile2.uti;

import android.content.Context;
import android.content.SharedPreferences;

public class PersonalDataHelper {
	final String prePreference = "presonal";
	final String preUid = "uid";
	final String prePwd = "pwd";
	
	SharedPreferences preferences = null; 
	
	public PersonalDataHelper(Context context){
		preferences = context.getSharedPreferences(prePreference, 0); 
	}
	
	public void setUid(String data){
		if (data == null)
			return;
		preferences.edit().putString(preUid, data).commit();
	}
	
	public String getUid(){
		return preferences.getString(preUid, null);
	}
	
	public void setPwd(String data){
		if (data == null)
			return;
		preferences.edit().putString(prePwd, data).commit();
	}
	
	public String getPwd(){
		return preferences.getString(prePwd, null);
	}
}
