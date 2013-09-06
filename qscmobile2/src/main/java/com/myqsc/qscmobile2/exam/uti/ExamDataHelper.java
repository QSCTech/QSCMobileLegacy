package com.myqsc.qscmobile2.exam.uti;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;

import com.myqsc.qscmobile2.network.DataUpdater;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.Utility;
import com.myqsc.qscmobile2.xiaoli.uti.XiaoliHelper;

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
		String result = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(DataUpdater.JW_KAOSHI, null);
        assert result != null;

        try {
            List<ExamStructure> list = new ArrayList<ExamStructure>();
            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i != jsonArray.length(); ++i) {
                ExamStructure examStructure = new ExamStructure(jsonArray.optJSONObject(i));
                if (examStructure.term.indexOf(term) != -1 || term == 0)
                    list.add(examStructure);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
	}

    public List<ExamStructure> getTodayExamList(Calendar calendar) {
        List<ExamStructure> today = new ArrayList<ExamStructure>();
        List<ExamStructure> list = getExamList((char) 0x0);
        for (ExamStructure structure : list) {
            if (structure.isToday(calendar)) {
                today.add(structure);
            }
        }
        return today;
    }

    public ExamStructure getCardExamStructure(Calendar calendar) {
        List<ExamStructure> list = getExamList((char) 0x0);
        ExamStructure examStructure = null;
        for (ExamStructure structure : list) {
            Calendar time = structure.getStartTime();
            if (time == null)
                continue;
            if (examStructure == null &&
                    time.getTimeInMillis() -
                        Calendar.getInstance().getTimeInMillis() > 0 &&
                    time.getTimeInMillis() -
                        Calendar.getInstance().getTimeInMillis() < 1000L * 60 * 60 * 24 * 30)
                examStructure = structure;
            else {
                if (examStructure != null &&
                        examStructure.getStartTime().getTimeInMillis() -
                                time.getTimeInMillis() > 0 &&
                        time.getTimeInMillis() -
                                Calendar.getInstance().getTimeInMillis() > 0) {
                    examStructure = structure;
                }
            }
        }
        return examStructure;
    }
}
