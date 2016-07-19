package com.sql.generator;

import java.io.Serializable;
import java.util.List;

public class Between implements Serializable {
	private static final long serialVersionUID = 1193050963392836719L;
	private Object min;
	private Object max;
	private boolean isNot = false; // 为 true 表示 not between

	/**
	 * 构造函数只有包访问权限
	 */
	Between(Object min, Object max, boolean isNot) {
		this.min = min;
		this.max = max;
		this.isNot = isNot;
	}

	Object getMin() {
		return min;
	}

	Object getMax() {
		return max;
	}

	boolean isNot() {
		return isNot;
	}

	String process(String column, List<Object> params) {
		String where = "";
		where += column;
		where += (this.isNot() ? " not between |\1| and |\1|" : " between |\1| and |\1|");
		params.add(this.getMin());
		params.add(this.getMax());
		return where;
	}
}
