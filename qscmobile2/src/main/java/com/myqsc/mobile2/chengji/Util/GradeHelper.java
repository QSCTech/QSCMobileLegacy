package com.myqsc.mobile2.chengji.Util;

import android.content.Context;

import com.myqsc.mobile2.network.DataUpdater;
import com.myqsc.mobile2.uti.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 13-9-25.
 */
public class GradeHelper {
    Context mContext = null;
    List<GradeClassStructure> grade = new ArrayList<GradeClassStructure>();
    List<GradeAverageStructure> average = new ArrayList<GradeAverageStructure>();

    public GradeHelper(Context context) {
        mContext = context;

        String data = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(DataUpdater.JW_CHENGJI, null);
        if (data == null) {
            return ;
        }


        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray gradeJSONArray = jsonObject.getJSONArray("chengji_array");
            for (int i = 0; i != gradeJSONArray.length(); ++i) {
                try {
                    grade.add(new GradeClassStructure(gradeJSONArray.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONArray averageJSONArray = jsonObject.getJSONArray("junji_array");
            for (int i = 0; i != averageJSONArray.length(); ++i) {
                try {
                    average.add(new GradeAverageStructure(averageJSONArray.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public GradeAverageStructure getTotalAverageGrade() {
        for (GradeAverageStructure structure : average) {
            if (structure.time.compareTo("所有课程") == 0) {
                return structure;
            }
        }
        return null;
    }
}
