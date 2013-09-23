package com.myqsc.mobile2.xiaoche.uti;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.myqsc.mobile2.network.DataUpdater;
import com.myqsc.mobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.mobile2.uti.Utility;

import android.content.Context;

public class XiaocheDataHelper {
	Context mContext = null;

	public XiaocheDataHelper(Context context){
		this.mContext = context;
	}
	
	public List<XiaocheStructure> getBus(final String start, final String stop) {
		String result = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(DataUpdater.COMMON_XIAOCHE, null);
        assert result != null;
        try {
            JSONArray jsonArray;
            jsonArray = new JSONArray(result);
            List<XiaocheStructure> list = new ArrayList<XiaocheStructure>();
            for(int i = 0; i != jsonArray.length(); ++i){
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                XiaocheStructure structure = new XiaocheStructure(jsonObject);
                if (structure.startPos.indexOf(start) != -1 && structure.stopPos.indexOf(stop) != -1){
                    list.add(structure);
                }
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
		}
        throw new RuntimeException("校车数据为空");
	}
}
