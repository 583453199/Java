package com.sqlgenerator;


public class ColumnRelationEquals extends ColumnRelation {
	private static final long serialVersionUID = 5106401731547353359L;

	/**
	 * 构造函数只有包访问权限
	 */
	ColumnRelationEquals(String leftColumn, String rightColumn) {
		super(leftColumn, Relation.EQUALS, rightColumn);
	}
}
