package com.myqsc.qscmobile2.fragment.cardlist;

import org.json.JSONException;
import org.json.JSONObject;

public class FunctionStructure {
	public int cardIcon = 0;
	public String cardName = null;
	public int iconRight = 0;
	
	public final static String PREFERENCE = "FUNCTIONLIST_STRUCTURE";
	
	public FunctionStructure(){
		
	}
	public FunctionStructure(JSONObject object) throws JSONException{
		cardIcon = object.getInt("cardIcon");
		cardName = object.getString("cardName");
		iconRight = object.getInt("iconRight");
	}
	
	public JSONObject toJsonObject() throws JSONException{
		JSONObject object = new JSONObject();
		object.put("cardIcon", cardIcon);
		object.put("cardName", cardName);
		object.put("iconRight", iconRight);
		return object;
	}
}
