package com.myqsc.qscmobile2.uti;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;

import com.myqsc.qscmobile2.network.DataUpdater;
import com.myqsc.qscmobile2.support.database.structure.ExamStructure;

@SuppressLint("NewApi")
public class ExamDataHelper {
	Context mContext = null;
	public ExamDataHelper(Context context) {
		this.mContext = context;
	}
	
	public void clear(){
		mContext.getSharedPreferences(Utility.PREFERENCE, 0).edit()
                .remove(DataUpdater.JW_KAOSHI).commit();
	}
	
	public List<ExamStructure> getExamList(final char term){
		Message message = new Message();
		String result = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(DataUpdater.JW_KAOSHI, null);
        assert result != null;
        try {
            List<ExamStructure> list = new ArrayList<ExamStructure>();
            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i != jsonArray.length(); ++i) {
                ExamStructure examStructure = new ExamStructure(jsonArray.optJSONObject(i));
                if (examStructure.term.indexOf(term) != -1)
                    list.add(examStructure);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
	}
}
