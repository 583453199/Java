package com.sql.generator;


import java.io.Serializable;

public class LeftJoin implements Serializable {
	private static final long serialVersionUID = 1L;
	private Query subQuery;
	private String on;
	private String alias;

	/**
	 * 构造函数只有包访问权限
	 */
	LeftJoin(Query subQuery, String on) {
		this.subQuery = subQuery;
		this.on = on;
	}

	/**
	 * 构造函数只有包访问权限
	 */
	LeftJoin(Query subQuery, String on, String alias) {
		this(subQuery, on);
		this.alias = alias;
	}

	Query getSubQuery() {
		return subQuery;
	}

	String getOn() {
		return on;
	}

	String getAlias() {
		return alias;
	}
}
