package com.zjj.util.json;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.zjj.util.DateUtil;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
/**
 * @version 1.0
 */
public class JsonDateValueProcessor implements JsonValueProcessor {

	private String format = "yyyy-MM-dd HH:mm:ss";

	public JsonDateValueProcessor() {

	}

	public JsonDateValueProcessor(String format) {
		this.format = format;
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		String[] obj = {};
		if (value == null)
			return obj;
		if (value instanceof java.util.Date[]) {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			java.util.Date[] dates = (java.util.Date[]) value;
			obj = new String[dates.length];
			for (int i = 0; i < dates.length; i++) {
				if ("dbox".equals(format)) {
					obj[i] = getDboxDateStr((java.util.Date) value);
				} else {
					obj[i] = sf.format(dates[i]);
				}
			}
		} else if (value instanceof java.sql.Date[]) {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			java.sql.Date[] dates = (java.sql.Date[]) value;
			obj = new String[dates.length];
			for (int i = 0; i < dates.length; i++) {
				obj[i] = sf.format(dates[i]);
			}
		}
		return obj;
	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		if (value == null)
			return "";
		// 注意：在判断几个父子级类型时要先判断子类型再判断父类型
		if (value instanceof java.sql.Date) {
			String str = new SimpleDateFormat(format)
					.format((java.sql.Date) value);
			return str;
		} else if (value instanceof java.sql.Timestamp
				|| value instanceof java.util.Date) {
			String str = "";
			if ("dbox".equals(format)) {
				str = getDboxDateStr((java.util.Date) value);
			} else {
				str = new SimpleDateFormat(format)
						.format((java.util.Date) value);
			}
			return str;
		}
		return value.toString();
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public static String getDboxDateStr(Date d) {
		String seconds_ago = "秒钟前";
		String minutes_ago = "分钟前";
		String hours_ago = "小时前";
		String yesterday = "昨天";
		String month_day = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(d);
		StringBuffer retStr = new StringBuffer("");
		int days = daysBetween(d);
		String hourMinute = DateUtil.format(d, " HH:mm");
		// 今天
		if (days == 0) {
			long second = secondeBetween(d);
			// 小于60秒
			if (second < 60) {
				retStr.append(second + " " + seconds_ago);
			} else if (second < 3600 && second >= 60) {
				retStr.append(Integer.parseInt(String.valueOf(second / 60))
						+ " " + minutes_ago);
			} else {
				retStr.append(Integer.parseInt(String.valueOf(second / 3600))
						+ " " + hours_ago);
			}
		} else if (days == 1) {// 昨天
			retStr.append(yesterday + " " + hourMinute);
		} else {
			retStr.append(month_day);
		}
		return retStr.toString();
	}

	private static int daysBetween(Date date) {
		long d1 = DateUtil.parseDate("yyyy-MM-dd",
				DateUtil.format(new Date(), "yyyy-MM-dd")).getTime();
		long d2 = DateUtil.parseDate("yyyy-MM-dd",
				DateUtil.format(date, "yyyy-MM-dd")).getTime();
		long days = (d1 - d2) / (1000 * 3600 * 24);
		return Math.abs(Integer.parseInt(String.valueOf(days)));
	}

	private static long secondeBetween(Date date) {
		long d1 = System.currentTimeMillis();
		long d2 = date.getTime();
		long seconde = (d1 - d2) / 1000;
		long time = Math.abs(seconde);
		return time == 0 ? 1 : time;
	}
}
