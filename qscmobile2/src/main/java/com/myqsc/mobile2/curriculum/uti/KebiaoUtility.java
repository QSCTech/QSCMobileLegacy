package com.myqsc.mobile2.curriculum.uti;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by richard on 13-8-31.
 */
public class KebiaoUtility {
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
     * @return Map
     */
    public static Map<Integer, Object> getDiffTime(Calendar calendar,
                                  List<KebiaoClassData> list) {
        if (list == null || list.size() == 0)
            return null;
        /**
         * map 1：时间差， 2：课程节数，3：一节课对象
         */
        final Map<Integer, Object> map = new HashMap<Integer, Object>();

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
                Calendar toCalendar = Calendar.getInstance();
                toCalendar.set(Calendar.HOUR_OF_DAY,
                        Integer.parseInt(classTo[course.get(i)].substring(0, 2)));
                toCalendar.set(Calendar.MINUTE,
                        Integer.parseInt(classTo[course.get(i)].substring(3, 5)));
                toCalendar.set(Calendar.SECOND, 0);
                map.put(1, (int) ((toCalendar.getTimeInMillis() - calendar.getTimeInMillis()) / 1000));
                map.put(2, course.get(i));
                break;
            }
        }

        if (map.get(1) == null) {
            for (int i = 0; i != course.size(); ++i) {
                if (date.compareTo(classFrom[course.get(i)]) < 0) {
                    Calendar fromCalendar = Calendar.getInstance();
                    fromCalendar.set(Calendar.HOUR_OF_DAY,
                            Integer.parseInt(classFrom[course.get(i)].substring(0, 2)));
                    fromCalendar.set(Calendar.MINUTE,
                            Integer.parseInt(classFrom[course.get(i)].substring(3, 5)));
                    fromCalendar.set(Calendar.SECOND, 0);
                    map.put(1, (int) ((calendar.getTimeInMillis() - fromCalendar.getTimeInMillis()) / 1000));
                    map.put(2, course.get(i));
                    break;
                }
            }
        }

        if (!map.containsKey(2))
            return null; //今天没有课

        int course_num = (Integer) map.get(2);
        for(KebiaoClassData data : list)
            if (data.inRange(course_num))
                map.put(3, data);
        return map;
    }


    /**
     * 输入一个课程对象，传出标准的“周一 6/7/8"这种格式的字符串
     * @param kebiaoClassData
     * @return
     */
    public static String processClassTime(KebiaoClassData kebiaoClassData) {
        final String[] week = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        String string = week[kebiaoClassData.time];
        for(int i : kebiaoClassData.classes) {
            string += i + ", ";
        }
        string = string.substring(0, string.length() - 2);
        try {
            //添加上课下课时间
            string += " (" + classFrom[kebiaoClassData.classes[0]] +
                    " - " + classTo[kebiaoClassData.classes[kebiaoClassData.classes.length - 1]] + ")";
        }catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }


        return string;
    }

    public static String processTimeTitle(Calendar calendar) {
        final String[] week = {"", "周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        String string = (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        string += " " + week[calendar.get(Calendar.DAY_OF_WEEK)];
        return string;
    }
}
