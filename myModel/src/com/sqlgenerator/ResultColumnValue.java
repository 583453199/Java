package com.sqlgenerator;


import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * <pre>
 * 一个Generatable支持多个ResultColumnValue, 允许从其前面多个Generatable获取值
 * 
 * a) 获取插入语句的 last_insert_id 
 * {
 *     resultCallback: 41bdffd2-8a53-4765-a70a-8cabbaf27496
 *     columnName: _lastInsertId
 * }
 * 
 * b) 获取查询语句的返回值（支持获取到1行或者多行）
 * {
 *     resultCallback: af3391b1-7709-40ac-85bc-424a0447dc38
 *     columnName: score,
 *     defaultValue: -91
 * }
 * </pre>
 */
public class ResultColumnValue implements Serializable {
	private static final long serialVersionUID = -8510314464015753491L;
	private String columnName;
	private String resultCallback;
	// 如果是获取查询语句的返回值，少于1行记录，有默认值，使用默认值，默认报错，查询失败
	private Object defaultValue;

	public transient static final String KEY_COLUMN_NAME = "columnName";
	public transient static final String KEY_RESULTCALLBACK = "resultCallback";
	public transient static final String KEY_DEFAULT_VALUE = "defaultValue";
	public transient static final String KEY_LAST_INSERT_ID = "_lastInsertId";

	/**
	 * 构造函数只有包访问权限
	 */
	ResultColumnValue() {
	}

	/**
	 * 构造函数只有包访问权限
	 */
	ResultColumnValue(Generatable<?> generatable, String columnName, Object defaultValue) {
		if (generatable.getResultCallback() == null) {
			generatable.setResultCallback(UUID.randomUUID().toString());
		}
		this.columnName = columnName;
		this.resultCallback = generatable.getResultCallback();
		this.defaultValue = defaultValue;
	}

	/**
	 * 转成json时候，需要get方法为public，否则报 No serializer found for class
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * 转成json时候，需要get方法为public，否则报 No serializer found for class
	 */
	public String getResultCallback() {
		return resultCallback;
	}

	/**
	 * 转成json时候，需要get方法为public，否则报 No serializer found for class
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	String process(String column, List<Object> params) {
		String where = "";
		where += column;
		where += " |\1|";
		params.add(this);
		return where;
	}
}
