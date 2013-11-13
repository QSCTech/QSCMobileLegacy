package com.myqsc.mobile2.Utility;

import java.util.Calendar;

public class TimeUtils {

    public static Calendar clearTime(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }

    // This function includes the left boundary while excludes the right boundary of the date range.
    public static Boolean inRange(Calendar time, Calendar start, Calendar end) {
        return !time.before(start) && time.before(end);
    }

}
