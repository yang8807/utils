package com.magnify.yutils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ʱ��Ĺ�����
 *
 * @author ldy
 */
public class DateUtils {

	final public static String FORMAT_STRING_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	final public static String FORMAT_STRING_DATE_YEAR_MONTH = "yyyy-MM";

	final public static String FORMAT_STRING_DATE_DAY = "dd";

	final public static String FORMAT_STRING_DATE_ALL_TIME = "HH:mm:ss";

	final public static String FORMAT_STRING_DATE = "yyyy-MM-dd";

	final public static String FORMAT_STRING_DATE_CHINESE = "yyyy��MM��dd��";

	final public static String FORMAT_STRING_DATE_TIME_POINT = "yyyy.MM.dd HH:mm";

	final public static String FORMAT_STRING_DATE_MONTH_DAY = "MM��dd��";

	final public static String FORMAT_STRING_DATE_MONTH = "MM��";

	public static Date stringToDate(String dateString, String format) {
		try {
			return new SimpleDateFormat(format).parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date stringToDate(String date) {
		return stringToDate(date, FORMAT_STRING_DATE_TIME);
	}

	public static String dateToString(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	public static String dateToString(Date date) {
		return dateToString(date, FORMAT_STRING_DATE_TIME);
	}

	public static String calendarToString(Calendar calendar) {
		return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE);
	}

	public static Calendar stringToCalendar(String dateString) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.stringToDate(dateString));
		return calendar;
	}

	public static String toString(int year, int monthOfYear, int dayOfMonth) {
		String date = year + "-";
		if (monthOfYear < 9) {
			date += "0";
		}
		date += (monthOfYear + 1) + "-";
		if (dayOfMonth < 10) {
			date += "0";
		}
		return date + dayOfMonth;
	}

	public static String todayString() {
		return calendarToString(Calendar.getInstance());
	}

	/**
	 * ��ָ���ĸ�ʽ���ص�ǰ��ʾ��ǰʱ����ַ���
	 */
	public static String nowString(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	public static String nowString() {
		return nowString(FORMAT_STRING_DATE_TIME);
	}

	/**
	 * �жϵ�һ�������Ƿ����ڵڶ�������
	 * @return ���ڷ���true�����ڻ���ڷ���false
	 */
	public static boolean isLaterThan(String firstDate, String secondDate) {
		return stringToCalendar(firstDate).getTimeInMillis() > stringToCalendar(secondDate).getTimeInMillis();
	}

	/**
	 * ��ȡʱ���������ʱ����Ϣ��
	 */
	public static long getTimeStamp() {
		return System.currentTimeMillis();
	}

	/**
	 * ��ȡʱ���������ʱ����Ϣ��
	 */
	public static long getTimeStamp(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month, day);
		return calendar.getTimeInMillis();
	}

	/**
	 * ��ʱ���ת��Ϊ�ɶ����ַ���
	 */
	public static String stampToString(long stamp) {
		return dateToString(new Date(stamp));
	}

	/**
	 * ��ʱ�������ָ����ʽת��Ϊ�ɶ����ַ���
	 */
	public static String stampToString(long stamp, String format) {
		return dateToString(new Date(stamp), format);
	}

	/**
	 * ���ص�ǰ�����ڼ���
	 *
	 * @return ����һ�����������������ڼ���
	 */
	public static String getDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		String day = "";
		switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				day = "����һ";
				break;
			case Calendar.TUESDAY:
				day = "���ڶ�";
				break;
			case Calendar.WEDNESDAY:
				day = "������";
				break;
			case Calendar.THURSDAY:
				day = "������";
				break;
			case Calendar.FRIDAY:
				day = "������";
				break;
			case Calendar.SATURDAY:
				day = "������";
				break;
			case Calendar.SUNDAY:
				day = "������";
				break;

			default:
				break;
		}
		return day;
	}

	/**
	 * ���ص�ǰ�����ڼ���
	 *
	 * @return ����һ�����������������ڼ���
	 */
	public static String getDayOfWeekForPickerDialog(int year, int month, int dayOfMonth) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, dayOfMonth);
		String day = "";
		switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				day = "��һ";
				break;
			case Calendar.TUESDAY:
				day = "�ܶ�";
				break;
			case Calendar.WEDNESDAY:
				day = "����";
				break;
			case Calendar.THURSDAY:
				day = "����";
				break;
			case Calendar.FRIDAY:
				day = "����";
				break;
			case Calendar.SATURDAY:
				day = "����";
				break;
			case Calendar.SUNDAY:
				day = "����";
				break;

			default:
				break;
		}
		return day;
	}

	/**
	 * �����Ѻõ�ʱ���ʽ,����ǽ��죬�򷵻ؽ��죬��������죬�ͷ������죬�����������ǰ�ģ��ͷ���yyyy-mm-dd
	 *
	 */
	public static String friendTime(Date time) {
		return friendTimeBase(time, FORMAT_STRING_DATE);
	}

	/**
	 * �����Ѻõ�ʱ���ʽ,����ǽ��죬�򷵻ؽ��죬��������죬�ͷ������죬�����������ǰ�ģ��ͷ���dd
	 */
	public static String friendTimeForDay(Date time) {
		return friendTimeBase(time, FORMAT_STRING_DATE_DAY);
	}

	public static String friendTimeBase(Date time, String type) {
		Calendar now = Calendar.getInstance();
		Calendar target = Calendar.getInstance();
		target.setTime(time);
		int day_sub = Math.abs(now.get(Calendar.DAY_OF_MONTH) - target.get(Calendar.DAY_OF_MONTH));

		if (now.get(Calendar.YEAR) == target.get(Calendar.YEAR)) {// ������һ��
			if (now.get(Calendar.MONTH) == target.get(Calendar.MONTH)) {// ����·�һ��
				if (day_sub == 0) {// �����ͬһ��
					// ���ؽ���
					return "����";
				} else if (day_sub == 1) { //
					// ��������
					return "����";
				}
			}
		}
		return dateToString(time, type.equals(FORMAT_STRING_DATE) ? FORMAT_STRING_DATE : FORMAT_STRING_DATE_DAY);
	}

	public static String chatFriendTime(Date time) {
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
					return "���� " + dateToString(time, "HH:mm");
				} else if (day_sub == 1) { //
					// ��������
					return "���� " + dateToString(time, "HH:mm");
				}
			}
		}
		return dateToString(time, DateUtils.FORMAT_STRING_DATE_TIME_POINT);
	}

	/**
	 * ���������Ϣ�ǽ��������ʾ8��20��,���������Ϣ��ȥ�������ʾ2013��8��20��
	 */
	public static String friendTimeMMDD(Date time) {
		Calendar now = Calendar.getInstance();
		Calendar target = Calendar.getInstance();
		target.setTime(time);
		if (now.get(Calendar.YEAR) == target.get(Calendar.YEAR)) {
			return dateToString(time, FORMAT_STRING_DATE_MONTH_DAY);
		} else {
			return dateToString(time, FORMAT_STRING_DATE_CHINESE);
		}
	}

	/**
	 * ���Ѻõķ�ʽ��ʾʱ��
	 */
	public static String friendly_time(Date time) {
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// �ж��Ƿ���ͬһ��
		String curDate = dateToString(cal.getTime(), FORMAT_STRING_DATE);
		String paramDate = dateToString(time, FORMAT_STRING_DATE);
		if (curDate.equals(paramDate)) {
			long t = cal.getTimeInMillis() - time.getTime();
			int min = (int) (t / 60000);
			int hour = (int) (t / 3600000);
			if (min == 0)
				ftime = Math.max(t / 1000, 1) + "��ǰ";
			else if (hour == 0)
				ftime = Math.max(min, 1) + "����ǰ";
			else
				ftime = hour + "Сʱǰ";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			long t = cal.getTimeInMillis() - time.getTime();
			int min = (int) (t / 60000);
			int hour = (int) (t / 3600000);
			if (min == 0)
				ftime = Math.max(t / 1000, 1) + "��ǰ";
			else if (hour == 0)
				ftime = Math.max(min, 1) + "����ǰ";
			else
				ftime = hour + "Сʱǰ";
		} else if (days == 1) {
			ftime = "����";
		} else if (days == 2) {
			ftime = "ǰ��";
		} else if (days > 2) {
			ftime = dateToString(time, FORMAT_STRING_DATE);
		}
		return ftime;
	}

	/**
	 * �����Ѻõ�ʱ���ʽ,����Ǳ��£��ͷ��ر��£����򷵻�yyyy-MM��ʽ
	 *
	 * @param time ʱ��
	 * @param type ��ʽ
	 */
	public static String friendlyMonth(Date time, String type) {
		Calendar now = Calendar.getInstance();
		Calendar target = Calendar.getInstance();
		if (null == time) {
			time = new Date();
		}
		target.setTime(time);
		if (now.get(Calendar.YEAR) == target.get(Calendar.YEAR)) {// ������һ��
			if (now.get(Calendar.MONTH) == target.get(Calendar.MONTH)) {// ����·�һ��
				return "����";
			}
		}
		return dateToString(time, type == null ? FORMAT_STRING_DATE_MONTH : type);
	}

	public static boolean isSameMonth(Date firstTime, Date secondTime) {
		Calendar first = Calendar.getInstance();
		Calendar second = Calendar.getInstance();
		if (null == firstTime) {
			firstTime = new Date();
		}
		if (null == secondTime) {
			secondTime = new Date();
		}
		first.setTime(firstTime);
		second.setTime(secondTime);
		if (first.get(Calendar.YEAR) == second.get(Calendar.YEAR)) {// ������һ��
			if (first.get(Calendar.MONTH) == second.get(Calendar.MONTH)) {// ����·�һ��
				return true;
			}
		}
		return false;
	}

	/**
	 * @see TimeDuring
	 */
	public static TimeDuring getTimeDuring(long timestamp) {
		return new TimeDuring(timestamp);
	}

	/**
	 * ������תΪ�졢Сʱ�����ӡ���,���ɽ��мӼ�����������ڼ�ʱ�͵���
	 *
	 * @see DateUtils#getTimeDuring(long)
	 * @author ����
	 */
	public static class TimeDuring {

		/**
		 * ʱ���
		 */
		private long mTimestamp;

		private TimeDuring(long timestamp) {
			mTimestamp = timestamp;
		}

		/**
		 * ������ʱ�����(��ͨ���������޸�ʱ��)
		 *
		 * @param timestamp ����
		 */
		public void applyTime(long timestamp) {
			mTimestamp += timestamp;
		}

		/**
		 * ���ص�ǰʱ���
		 *
		 * @return
		 */
		public long getTime() {
			return mTimestamp;
		}

		/**
		 * ��
		 *
		 * @see #days
		 * @return
		 */
		public long days() {
			return mTimestamp / (1000 * 60 * 60 * 24);
		}

		/**
		 * Сʱ
		 *
		 * @see #hours
		 * @return
		 */
		public int hours() {
			return (int) (mTimestamp % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		}

		/**
		 * ����
		 *
		 * @see #minutes
		 * @return
		 */
		public int minutes() {
			return (int) (mTimestamp % (1000 * 60 * 60)) / (1000 * 60);
		}

		/**
		 * ��
		 *
		 * @see #seconds
		 * @return
		 */
		public int seconds() {
			return (int) (mTimestamp % (1000 * 60)) / 1000;
		}

	}

}
