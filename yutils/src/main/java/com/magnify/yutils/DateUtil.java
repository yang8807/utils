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

        if (now.get(Calendar.YEAR) == target.get(Calendar.YEAR)) {// ������һ��
            if (now.get(Calendar.MONTH) == target.get(Calendar.MONTH)) {// ����·�һ��
                if (day_sub == 0) {// �����ͬһ��
                    // ���ؽ���
                    return dateToString(time, "HH:mm");
                }
                // else if (day_sub == 1) { //
                // // ��������
                // return "���� " + dateToString(time, "HH:mm");
                // }
            }
        }
        return dateToString(time, "MM��dd�� HH:mm");
    }

    /**
     * ����·�
     *
     * @param timestamp ʱ���
     * @return 1�·���0��������1���Դ�����
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
     * ��ʽ������
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat fm = new SimpleDateFormat(pattern);
        return fm.format(date);
    }
}
