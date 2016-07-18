package com.sqlgenerator;


import java.io.Serializable;

public class UnionQuery implements Serializable {
	private static final long serialVersionUID = -7769673755624792422L;
	private Query query;
	private boolean unionAll = false; // true 使用 union all， false 使用 union

	/**
	 * 构造函数只有包访问权限
	 */
	UnionQuery(Query query, boolean unionAll) {
		this.query = query;
		this.unionAll = unionAll;
	}

	boolean isUnionAll() {
		return unionAll;
	}

	Query getQuery() {
		return query;
	}

}
