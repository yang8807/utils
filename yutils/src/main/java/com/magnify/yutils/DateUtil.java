package com.magnify.yutils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil extends DateUtils {

    public static String friendTime(Date time) {
        LogUtil.i("DateUtil", "time:%s", dateToString(time, "yy-MM-dd HH:mm:ss"));
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        if (null == time) {
            time = new Date();
        }
        target.setTime(time);
        int day_sub = Math.abs(now.get(Calendar.DAY_OF_MONTH) - target.get(Calendar.DAY_OF_MONTH));

        if (now.get(Calendar.YEAR) == target.get(Calendar.YEAR)) {// 如果年份一样
            if (now.get(Calendar.MONTH) == target.get(Calendar.MONTH)) {// 如果月份一样
                if (day_sub == 0) {// 如果是同一天
                    // 返回今天
                    return dateToString(time, "HH:mm");
                }
                // else if (day_sub == 1) { //
                // // 返回昨天
                // return "昨天 " + dateToString(time, "HH:mm");
                // }
            }
        }
        return dateToString(time, "MM月dd日 HH:mm");
    }

    /**
     * 获得月份
     *
     * @param timestamp 时间戳
     * @return 1月返回0，二月则1，以此类推
     */
    public static String getYearAndMonth(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
    }

    public static String getYear(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.YEAR) + "";
    }

    /**
     * 格式化日期
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat fm = new SimpleDateFormat(pattern);
        return fm.format(date);
    }
}
