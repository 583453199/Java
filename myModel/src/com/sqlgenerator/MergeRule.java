package com.sqlgenerator;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class MergeRule implements Serializable {
	private static final long serialVersionUID = -7178608667063344173L;
	private Generatable<?> merge;
	private Generatable<?> into;
	private String mergeCol;
	private String intoCol;
	private boolean oneToMany;

	public static final String KEY_MERGE = "merge";
	public static final String KEY_INTO = "into";
	public static final String KEY_MERGECOL = "mergeCol";
	public static final String KEY_INTOCOL = "intoCol";
	public static final String KEY_ONE_TO_MANY = "oneToMany";

	/**
	 * 构造函数只有包访问权限
	 */
	MergeRule() {
	}

	/**
	 * 构造函数只有包访问权限
	 */
	MergeRule(Generatable<?> merge, Generatable<?> into, String mergeCol, String intoCol, boolean oneToMany) {
		this.merge = merge;
		this.into = into;
		this.mergeCol = mergeCol;
		this.intoCol = intoCol;
		this.oneToMany = oneToMany;
		// merge 禁止提供默认limit
		merge.setProhibitDefaultLimit(true);
	}

	Generatable<?> getMerge() {
		return merge;
	}

	public Map<String, Object> getRuleMap() {
		Map<String, Object> rule = new LinkedHashMap<String, Object>();
		rule.put(KEY_MERGE, this.merge.getResultCallback());
		rule.put(KEY_INTO, this.into.getResultCallback());
		rule.put(KEY_MERGECOL, this.mergeCol);
		rule.put(KEY_INTOCOL, this.intoCol);
		rule.put(KEY_ONE_TO_MANY, this.oneToMany);

		return rule;
	}
}
