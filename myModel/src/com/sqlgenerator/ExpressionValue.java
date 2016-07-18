package com.sqlgenerator;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionValue {
	private static final String CHAR_ALLOW = "[*a-zA-Z0-9_.+-/():,% \"]+";
	private static final Pattern PATTERN_ALLOW_CHAR = Pattern.compile(CHAR_ALLOW);
	private String value;

	/**
	 * 构造函数只有包访问权限
	 */
	ExpressionValue(String value) {
		Matcher matcher = PATTERN_ALLOW_CHAR.matcher(value);
		if (matcher.matches() == false) {
			throw new IllegalArgumentException("只允许 " + CHAR_ALLOW + " 范围内的字符, value=" + value);
		}
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
