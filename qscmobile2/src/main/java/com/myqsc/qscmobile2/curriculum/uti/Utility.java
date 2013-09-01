package com.myqsc.qscmobile2.curriculum.uti;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by richard on 13-8-31.
 */
public class Utility {
    public final static String[] classFrom = new String[]{
            "00:00", "08:00", "08:50", "09:50", "10:40", "11:30", "13:15", "14:05", "14:55", "15:55", "16:45", "18:30", "19:20", "20:10"
    };

    public final static String[] classTo = new String[]{
            "", "08:45", "09:35", "10:35", "11:25", "12:15", "14:00", "14:50", "15:40", "16:40", "17:30", "19:15", "20:05", "20:55"
    };

    public static int getWhichClass(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String date = simpleDateFormat.format(calendar.getTime());

        for (int i = 1; i != classFrom.length; ++i) {
            if (date.compareTo(classFrom[i]) > 0 && date.compareTo(classTo[i]) < 0) {
                try {
                    Calendar toCalendar = Calendar.getInstance();
                    toCalendar.clear();
                    toCalendar.setTime(simpleDateFormat.parse(classTo[i]));
                    return (int) ((toCalendar.getTimeInMillis() - calendar.getTimeInMillis()) / 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        for (int i = 1; i != classFrom.length; ++i) {
            if (date.compareTo(classFrom[i]) < 0) {
                try {
                    Calendar fromCalendar = Calendar.getInstance();
                    fromCalendar.clear();
                    fromCalendar.setTime(simpleDateFormat.parse(classFrom[i]));
                    return -(int) ((fromCalendar.getTimeInMillis() - calendar.getTimeInMillis()) / 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}
