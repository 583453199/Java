package com.zjj.util.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	public static final String C_DATE_DIVISION = "-";

	public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public static final String C_TIME_DATEBOOK_DEFAULT = "yyyy-MM-dd HH:mm";
	public static final String C_DATE_PATTON_DEFAULT = "yyyy-MM-dd";
	public static final String C_DATA_PATTON_YYYYMMDD = "yyyyMMdd";
	public static final String C_TIME_PATTON_HHMMSS = "HH:mm:ss";
	public static final String C_DATE_PATTON_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String C_DATE_PATTON_DDMMYYYY = "dd/MM/yyyy";

	public static final int C_ONE_SECOND = 1000;
	public static final int C_ONE_MINUTE = 60 * C_ONE_SECOND;
	public static final int C_ONE_HOUR = 60 * C_ONE_MINUTE;
	public static final long C_ONE_DAY = 24 * C_ONE_HOUR;

	// 星期表示数组
	public static final String[] WEEKDAY_LONG_EN = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			"Saturday" };
	public static final String[] WEEKDAY_SHORT_EN = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	public static final String[] WEEKDAY_CH = { "日", "一", "二", "三", "四", "五", "六" };

	/**
	 * Return the current date
	 * 
	 * @return － DATE<br>
	 */
	public static Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();
		return currDate;
	}

	/**
	 * 将20090510102050转成Date类型 对应日期2009-05-10 10:20:50
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parseBossDate(String dateStr) {
		SimpleDateFormat formate = new SimpleDateFormat(DateUtil.C_DATE_PATTON_YYYYMMDDHHMMSS);
		try {
			return formate.parse(dateStr);
		} catch (ParseException e) {
			// loggerService.error("", e);
			return null;
		}
	}

	/**
	 * Return the current date string
	 * 
	 * @return － 产生的日期字符串<br>
	 */
	public static String getCurrentDateStr() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		return format(currDate);
	}

	/**
	 * Return the current date in the specified format
	 * 
	 * @param strFormat
	 * @return
	 */
	public static String getCurrentDateStr(String strFormat) {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		return format(currDate, strFormat);
	}

	/**
	 * Parse a string and return a date value
	 * 
	 * @param dateValue
	 * @return
	 * @throws Exception
	 */
	public static Date parseDate(String dateValue) {
		return parseDate(C_DATE_PATTON_DEFAULT, dateValue);
	}

	/**
	 * Parse a strign and return a datetime value
	 * 
	 * @param dateValue
	 * @return
	 */
	public static Date parseDateTime(String dateValue) {
		return parseDate(C_TIME_PATTON_DEFAULT, dateValue);
	}

	/**
	 * Parse a string and return the date value in the specified format
	 * 
	 * @param strFormat
	 * @param dateValue
	 * @return
	 * @throws ParseException
	 * @throws Exception
	 */
	public static Date parseDate(String strFormat, String dateValue) {
		if (dateValue == null || "".equals(dateValue))
			return null;

		if (strFormat == null)
			strFormat = C_TIME_PATTON_DEFAULT;

		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		Date newDate = null;

		try {
			newDate = dateFormat.parse(dateValue);
		} catch (ParseException pe) {
			// loggerService.error("", pe);
			newDate = null;
		}

		return newDate;
	}

	/**
	 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串。
	 * 
	 * @param aTs_Datetime
	 *            需要转换的日期。
	 * @return 转换后符合给定格式的日期字符串
	 */
	public static String format(Date aTs_Datetime) {
		return format(aTs_Datetime, C_DATE_PATTON_DEFAULT);
	}

	/**
	 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串。
	 * 
	 * @param aTs_Datetime
	 *            需要转换的日期。
	 * @return 转换后符合给定格式的日期字符串
	 */
	public static String formatTime(Date aTs_Datetime) {
		return format(aTs_Datetime, C_TIME_PATTON_DEFAULT);
	}

	/**
	 * 将Date类型的日期转换为系统参数定义的格式的字符串。
	 * 
	 * @param aTs_Datetime
	 * @param as_Pattern
	 * @return
	 */
	public static String format(Date aTs_Datetime, String as_Pattern) {
		if (aTs_Datetime == null || as_Pattern == null)
			return null;

		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Pattern);

		return dateFromat.format(aTs_Datetime);
	}

	/**
	 * @param aTs_Datetime
	 * @param as_Format
	 * @return
	 */
	public static String formatTime(Date aTs_Datetime, String as_Format) {
		if (aTs_Datetime == null || as_Format == null)
			return null;

		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Format);

		return dateFromat.format(aTs_Datetime);
	}

	public static String getFormatTime(Date dateTime) {
		return formatTime(dateTime, C_TIME_PATTON_HHMMSS);
	}

	/**
	 * @param aTs_Datetime
	 * @param as_Pattern
	 * @return
	 */
	public static String format(Timestamp aTs_Datetime, String as_Pattern) {
		if (aTs_Datetime == null || as_Pattern == null)
			return null;

		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Pattern);

		return dateFromat.format(aTs_Datetime);
	}

	/**
	 * 查询条件的结束时间
	 * 
	 * @param endDate
	 * @return
	 */
	public static Date parseEndDate(String endDate) {

		if (null == endDate || "".equals(endDate.trim())) {
			return null;
		}
		return endOfTheDay(parseDate(endDate));

	}

	/**
	 * 获取今天00点:00分:00秒
	 * 
	 * @return
	 */
	public static Date getTodayZero() {
		return DateUtil.parseDate(DateUtil.getCurrentDateStr("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 某天的00点:00分:00秒
	 * 
	 * @param date
	 * @return java.util.Date
	 */
	public static Date beginOfTheDay(Date startTime) {
		if (startTime == null)
			return null;
		return DateUtil.parseDate(DateUtil.C_TIME_PATTON_DEFAULT,
				DateUtil.formatTime(startTime, DateUtil.C_TIME_PATTON_DEFAULT).substring(0, 11) + "00:00:00");
	}

	/**
	 * 某天的23点:59分:59秒
	 * 
	 * @param date
	 * @return java.util.Date
	 */
	public static Date endOfTheDay(Date endTime) {
		if (endTime == null)
			return null;
		return DateUtil.parseDate(DateUtil.C_TIME_PATTON_DEFAULT,
				DateUtil.formatTime(endTime, DateUtil.C_TIME_PATTON_DEFAULT).substring(0, 11) + "23:59:59");
	}

	/**
	 * 获取本月第一天的00点:00分:00秒
	 * 
	 * @return java.util.Date
	 */
	public static Date beginOfThisMonth() {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.get(Calendar.YEAR);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String startOfMonth = gregorianCalendar.get(Calendar.YEAR) + "-" + (gregorianCalendar.get(Calendar.MONTH) + 1)
				+ "-1";
		try {
			return simpleDateFormat.parse(startOfMonth);
		} catch (ParseException e) {
			// loggerService.error("", e);
		}
		return null;
	}

	public static Long getBetweenTime(Long startTime, Long endTime) {

		return (endTime - startTime) / 1000;

	}

	public static Date getNowDateTime(Date now) {

		String nowTimeStr = formatTime(now);

		return parseDateTime(nowTimeStr);

	}

	// 获得当前系统n月前的日期
	public static String lastNMonth(int Nmonth) {
		Date date = new Date();// 当前日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 格式化对象
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.MONTH, Nmonth);// 月份减一
		return sdf.format(calendar.getTime());
	}

	public static String dateToStr(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(date);
		return str;
	}

	public static Date AddOneDay(String date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date middle = parseDate("yyyy-MM-dd", date);
		String tempDate = df.format(middle.getTime() + 1 * 24 * 60 * 60 * 1000);
		return parseDate("yyyy-MM-dd", tempDate);
	}

	/**
	 * 当前日期之前的n天日期 n为整数
	 * 
	 * @param date
	 * @return
	 */
	public static Date AddDay(int n) {
		long nowTime = System.currentTimeMillis();
		return new Date(nowTime - n * 24 * 60 * 60 * 1000);
	}

	public static Date AddOneDay(String format, String date) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return DateUtil.parseDate(format,
				df.format(DateUtil.parseDate(format, date).getTime() + 1 * 24 * 60 * 60 * 1000));
	}

	public static String getXmlTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyyMMddHHmmssSSSS");
		return sdf.format(new Date());

	}

	/**
	 * 获得当月的第一天, 如2009-10-01 00:00:00
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(Calendar.DAY_OF_MONTH, gc.getActualMinimum(Calendar.DAY_OF_MONTH));
		gc.set(Calendar.HOUR_OF_DAY, gc.getActualMinimum(Calendar.HOUR_OF_DAY));
		gc.set(Calendar.MINUTE, gc.getActualMinimum(Calendar.MINUTE));
		gc.set(Calendar.SECOND, gc.getActualMinimum(Calendar.SECOND));
		return gc.getTime();
	}

	/**
	 * 获得当月的最后一天, 如2009-10-30 23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(Calendar.DAY_OF_MONTH, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
		gc.set(Calendar.HOUR_OF_DAY, gc.getActualMaximum(Calendar.HOUR_OF_DAY));
		gc.set(Calendar.MINUTE, gc.getActualMaximum(Calendar.MINUTE));
		gc.set(Calendar.SECOND, gc.getActualMaximum(Calendar.SECOND));
		return gc.getTime();
	}

	/**
	 * 获取某周第一天日期 00点:00分:00秒
	 * 
	 * @param date
	 *            这周中的一天
	 * @param start
	 *            1--第一天是周日， 2--第一天是周一， 类推
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date, int start) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - start;
		cal.add(Calendar.DATE, -day_of_week);

		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		return cal.getTime();
	}

	public static int getYear() {
		return new GregorianCalendar().get(Calendar.YEAR);
	}

	public static int getMonth() {
		return new GregorianCalendar().get(Calendar.MONTH) + 1;
	}

	public static String getMonthOfString() {
		int month = new GregorianCalendar().get(Calendar.MONTH) + 1;
		if (month < 10) {
			return "0" + month;
		}
		return month + "";
	}

	public static int getDay() {
		return new GregorianCalendar().get(Calendar.DATE);
	}

	public static String getDayOfString() {
		int day = new GregorianCalendar().get(Calendar.DATE);
		if (day < 10) {
			return "0" + day;
		}
		return day + "";
	}

	public static int getHour() {
		return new GregorianCalendar().get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute() {
		return new GregorianCalendar().get(Calendar.MINUTE);
	}

	public static int getSecond() {
		return new GregorianCalendar().get(Calendar.SECOND);
	}
}