package com.myqsc.qscmobile2.support.database.structure;

import org.json.JSONException;
import org.json.JSONObject;

public class UserIDStructure {
	public String uid = "";
	public String pwd = "";
	public boolean select = false;

    private final static String UID = "UID", PWD = "PWD", SELECT = "SELECT";

    public final static String PREFERENCE = "USERID";
	
	public UserIDStructure(){
	}

    public UserIDStructure(JSONObject jsonObject) {
        try {
            uid = jsonObject.getString(UID);
            pwd = jsonObject.getString(PWD);
            select = jsonObject.getBoolean(SELECT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
	
	public UserIDStructure(String uid, String pwd, boolean select) {
		this.uid = uid;
		this.pwd = pwd;
		this.select = select;
	}

    public JSONObject toJsonObject() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(UID, uid);
            jsonObject.put(PWD, pwd);
            jsonObject.put(SELECT, select);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
