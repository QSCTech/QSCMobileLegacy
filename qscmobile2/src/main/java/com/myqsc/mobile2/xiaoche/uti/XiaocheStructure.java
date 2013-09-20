package com.myqsc.mobile2.xiaoche.uti;

import org.json.JSONException;
import org.json.JSONObject;

public class XiaocheStructure {
	public final static String PREFERENCE = "xiaoche";
	
	public String bus_num, startPos, stopPos, startTime, stopTime, runTime, position, hint;
	
	public XiaocheStructure(){
		
	}
	
	public XiaocheStructure(JSONObject jsonObject) throws JSONException{
		bus_num = jsonObject.getString("车号");
		if (bus_num.indexOf("车") == -1)
			bus_num += "号车";
		startPos = jsonObject.getString("起点");
		stopPos = jsonObject.getString("终点");
		startTime = jsonObject.getString("发车时间");
		stopTime = jsonObject.getString("到站时间");
		runTime = jsonObject.getString("运行时间");
		position = jsonObject.getString("停靠地点");
		hint = jsonObject.getString("备注");
	}
	
	public JSONObject toJsonObject() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("车号", bus_num);
		jsonObject.put("起点", startPos);
		jsonObject.put("终点", stopPos);
		jsonObject.put("发车时间", startTime);
		jsonObject.put("到站时间", stopTime);
		jsonObject.put("运行时间", runTime);
		jsonObject.put("停靠地点", position);
		jsonObject.put("备注", hint);
		
		return jsonObject;
	}
}
