package com.sql.generator;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Insert extends Generatable<Insert> {
	private static final long serialVersionUID = 3040073936723619056L;
	private String table;
	private List<Condition> pairs = new ArrayList<Condition>();

	/**
	 * 构造函数只有包访问权限
	 */
	Insert(String table) {
		this.table = table;
	}

	public Insert value(String column, Object value) {
		return value(true, column, value);
	}

	public Insert valueIfNotEmpty(String column, Object value) {
		return value(Util.isNotEmpty(value), column, value);
	}

	public Insert valueIfNotNull(String column, Object value) {
		return value(value != null, column, value);
	}

	public Insert value(boolean conditionMatch, String column, Object value) {
		if (conditionMatch) {
			pairs.add(new Condition(column, value));
		}
		return this;
	}

	public Insert values(Map<String, Object> map) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			value(entry.getKey(), entry.getValue());
		}
		return this;
	}

	public Command getCommand() {
		this.statement = "insert into " + table + "(" + joinNames(pairs) + ") values (" + joinParams(pairs) + ")";
		this.params = joinValues(pairs);

		return new Command(statement, params);
	}
}