package com.myqsc.mobile2.xiaoche.uti;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class XiaocheStructure {
	public final static String PREFERENCE = "xiaoche";
	
	public String bus_num, startPos, stopPos, startTime, stopTime, runTime, hint;

    public String campus[] = null, position[] = null;
	
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
		hint = jsonObject.getString("备注");

        JSONArray jsonArray = jsonObject.getJSONArray("停靠地点");
        campus = new String[jsonArray.length()];
        position = new String[jsonArray.length()];

        for(int i = 0; i != jsonArray.length(); ++i) {
            JSONObject pos = jsonArray.optJSONObject(i);
            campus[i] = pos.getString("校区");
            position[i] = pos.getString("位置");
        }
	}
}
