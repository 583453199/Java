package com.sqlgenerator;


import java.io.Serializable;

public class ColumnRelation implements Serializable {
	private static final long serialVersionUID = 331464029467728464L;
	private String leftColumn;
	private Relation relation;
	private String rightColumn;

	/**
	 * 构造函数只有包访问权限
	 */
	ColumnRelation(String leftColumn, Relation relation, String rightColumn) {
		this.leftColumn = leftColumn;
		this.rightColumn = rightColumn;
		this.relation = relation;
	}

	String getLeftColumn() {
		return leftColumn;
	}

	Relation getRelation() {
		return relation;
	}

	String getRightColumn() {
		return rightColumn;
	}

	String process() {
		String where = "";
		where += this.getLeftColumn();
		where += this.getRelation();
		where += this.getRightColumn();
		return where;
	}

}
