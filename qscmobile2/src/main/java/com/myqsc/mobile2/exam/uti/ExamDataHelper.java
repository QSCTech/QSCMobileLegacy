package com.myqsc.mobile2.exam.uti;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.content.Context;

import com.myqsc.mobile2.network.DataUpdater;
import com.myqsc.mobile2.uti.Utility;

@SuppressLint("NewApi")
public class ExamDataHelper {
	Context mContext = null;



	public ExamDataHelper(Context context) {
		this.mContext = context;
	}

    List<ExamStructure> allExamList = null;
    char whichTerm = 0x0;
    public List<ExamStructure> getExamList(final char term){
        if (allExamList != null && whichTerm == term)
            return allExamList;

        whichTerm = term;
		String result = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(DataUpdater.JW_KAOSHI, null);
        allExamList = new ArrayList<ExamStructure>();

        try {
            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i != jsonArray.length(); ++i) {
                ExamStructure examStructure = new ExamStructure(jsonArray.optJSONObject(i));
                if ((examStructure.term.indexOf(term) != -1) || (term == 0x0)) {
                    allExamList.add(examStructure);
                }
            }
            return allExamList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allExamList;
	}

    List<ExamStructure> todayExamList = null;
    int whichDay = 0;

    /**
     * 获取每天的课表（带缓存）
     * @param calendar
     * @return
     */
    public List<ExamStructure> getTodayExamList(Calendar calendar) {
        if (whichDay == calendar.get(Calendar.DATE) && todayExamList != null)
            return todayExamList;

        whichDay = calendar.get(Calendar.DATE);
        todayExamList = new ArrayList<ExamStructure>();
        List<ExamStructure> list = getExamList((char) 0x0);
        for (ExamStructure structure : list) {
            if (structure.isToday(calendar)) {
                todayExamList.add(structure);
            }
        }
        return todayExamList;
    }

    List<ExamStructure> day30ExamList = null;
    int future30Day = 0;

    private List<ExamStructure> get30DayExamList(Calendar calendar) {
        if (future30Day == calendar.get(Calendar.DATE) && day30ExamList != null)
            return day30ExamList;

        future30Day = calendar.get(Calendar.DATE);
        day30ExamList = new ArrayList<ExamStructure>();

        for (ExamStructure structure : getExamList((char) 0x0)) {
            Calendar examCalendar = structure.getStartTime();
            if (examCalendar == null)
                continue;
//            LogHelper.d(examCalendar.toString());

            if ((int) ((examCalendar.getTimeInMillis() - calendar.getTimeInMillis()) / 1000 / 60 / 60 / 24) < 30 &&
                    examCalendar.compareTo(calendar) > 0) {
                day30ExamList.add(structure);
            }
        }
        Collections.sort(day30ExamList, new Comparator<ExamStructure>() {
            @Override
            public int compare(ExamStructure examStructure, ExamStructure examStructure2) {
                Calendar calendar1 = examStructure.getStartTime();
                Calendar calendar2 = examStructure2.getStartTime();

                if (calendar1 == null || calendar2 == null)
                    return 0;

                return calendar1.compareTo(calendar2);
            }
        });
        return day30ExamList;
    }

    public ExamStructure getCardExamStructure(Calendar calendar) {
        day30ExamList = get30DayExamList(calendar);

//        LogHelper.d(day30ExamList.size() + "个考试在未来30天内");
        for (ExamStructure structure : day30ExamList) {
            Calendar time = structure.getStartTime();
            if (time == null)
                continue;

            if (time.compareTo(calendar) > 0)
                return structure;
        }
        return null;
    }
}
