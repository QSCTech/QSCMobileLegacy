package com.myqsc.qscmobile2.curriculum.uti;

import com.myqsc.qscmobile2.uti.LogHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by richard on 13-8-31.
 */
public class Utility {
    public final static String[] classFrom = new String[]{
            "", "08:00", "08:50", "09:50", "10:40", "11:30", "13:15", "14:05", "14:55", "15:55", "16:45", "18:30", "19:20", "20:10"
    };

    public final static String[] classTo = new String[]{
            "", "08:45", "09:35", "10:35", "11:25", "12:15", "14:00", "14:50", "15:40", "16:40", "17:30", "19:15", "20:05", "20:55"
    };

    /**
     * 计算距离上课或下课的时间，下课时间为正，上课时间为负
     * @param calendar
     * @param list
     * @return
     */
    public static int getDiffTime(Calendar calendar,
                                  List<KebiaoClassData> list,
                                  KebiaoUpdateCallback callback
    ) {
        List<Integer> course = new ArrayList<Integer>();
        for(KebiaoClassData data : list) {
            for (int i : data.classes)
                course.add(i);
        }
//        LogHelper.i(calendar.toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String date = simpleDateFormat.format(calendar.getTime());

        for (int i = 0; i != course.size(); ++i) {
            if (date.compareTo(classFrom[course.get(i)]) >= 0
                    && date.compareTo(classTo[course.get(i)]) < 0) {
                LogHelper.d("in class");
                Calendar toCalendar = Calendar.getInstance();
                if (toCalendar.get(Calendar.SECOND) == 0) {
                    //计时到0了，该重新获取课表了
                    if (callback != null) {
                        boolean used = false;
                        for (KebiaoClassData data : list)
                            for (int num : data.classes)
                                if (num == course.get(i)) {
                                    used = true;
                                    callback.onNextKebiaoNeed(data);
                                }
                        if (!used)
                            callback.onNextKebiaoNeed(null);

                    }
                }
                toCalendar.set(Calendar.HOUR_OF_DAY,
                        Integer.parseInt(classTo[course.get(i)].substring(0, 2)));
                toCalendar.set(Calendar.MINUTE,
                        Integer.parseInt(classTo[course.get(i)].substring(3, 5)));
                toCalendar.set(Calendar.SECOND, 0);
                return (int) ((toCalendar.getTimeInMillis() - calendar.getTimeInMillis()) / 1000);
            }
        }

        for (int i = 0; i != course.size(); ++i) {
            if (date.compareTo(classFrom[course.get(i)]) < 0) {
                LogHelper.d("after class");
                Calendar fromCalendar = Calendar.getInstance();
                if (fromCalendar.get(Calendar.SECOND) == 0) {
                    //计时到0了，该重新获取课表了
                    if (callback != null) {
                        boolean used = false;
                        for (KebiaoClassData data : list)
                            for (int num : data.classes)
                                if (num == course.get(i)) {
                                    used = true;
                                    callback.onNextKebiaoNeed(data);
                                }
                        if (!used)
                            callback.onNextKebiaoNeed(null);
                    }
                }
                fromCalendar.set(Calendar.HOUR_OF_DAY,
                        Integer.parseInt(classFrom[course.get(i)].substring(0, 2)));
                fromCalendar.set(Calendar.MINUTE,
                        Integer.parseInt(classFrom[course.get(i)].substring(3, 5)));
                fromCalendar.set(Calendar.SECOND, 0);
                return (int) ((calendar.getTimeInMillis() - fromCalendar.getTimeInMillis()) / 1000);
            }
        }
        return 0;
    }
}
