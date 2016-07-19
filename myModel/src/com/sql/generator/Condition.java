package com.sql.generator;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Condition implements Serializable, Cloneable {
	private static final long serialVersionUID = -8789533750528374870L;
	protected Pref pref;
	private String name;
	private Object value;

	Pref getPref() {
		return pref;
	}

	String getName() {
		return name;
	}

	Object getValue() {
		return value;
	}

	/**
	 * 构造函数只有包访问权限
	 */
	Condition() {
	}

	/**
	 * 构造函数只有包访问权限
	 */
	Condition(GroupCondition value) {
		this.value = value;
	}

	/**
	 * 构造函数只有包访问权限
	 */
	Condition(Pref pref, GroupCondition value) {
		this.pref = pref;
		this.value = value;
	}

	/**
	 * 构造函数只有包访问权限
	 */
	Condition(Literal value) {
		this.value = value;
	}

	/**
	 * 构造函数只有包访问权限
	 */
	Condition(String name, Object value) {
		this.name = name;
		this.value = value;

		fixArrValue();
	}

	/**
	 * 构造函数只有包访问权限
	 */
	Condition(ColumnRelation value) {
		this.value = value;
	}

	/**
	 * 构造函数只有包访问权限
	 */
	Condition(Pref pref, ColumnRelation value) {
		this.pref = pref;
		this.value = value;
	}

	/**
	 * 构造函数只有包访问权限
	 */
	Condition(Pref pref, String name, Object value) {
		this.pref = pref;
		this.name = name;
		this.value = value;

		fixArrValue();
	}

	// 数组转成List
	private void fixArrValue() {
		if (value == null) {
			return;
		}

		if (value.getClass().isArray()) {
			List<Object> list = new LinkedList<Object>();
			Object[] objArr = (Object[]) value;
			for (Object obj : objArr) {
				list.add(obj);
			}
			value = list;
		}
	}

	String process(int index, String where, List<Object> params, boolean queryCount) {
		where = where.trim();
		Pref pref = this.getPref();
		// 第一个条件 或者 前面一个字符是"("， 不能加 and 和 or 前缀，
		if ((index == 0 || where.endsWith("(")) == false) {
			if (pref == Pref.AND) {
				where += " and";
			} else if (pref == Pref.OR) {
				where += " or";
			}
		}

		where += " ";
		String name = this.getName();
		Object value = this.getValue();
		if (value instanceof Query) { // 子查询
			Query subQuery = (Query) value;
			Query.initSubQuery(subQuery, queryCount);
			Command command = subQuery.getCommand();
			where += name;
			where += " (";
			where += command.getStatement();
			where += ")";
			for (Object param : command.getParams()) {
				params.add(param);
			}
		} else if (value instanceof ColumnRelation) {
			ColumnRelation columnRelation = (ColumnRelation) value;
			where += columnRelation.process();
		} else if (value instanceof Between) {
			Between between = (Between) value;
			where += between.process(name, params);
		} else if (value == Literal.IS_NULL || value == Literal.IS_NOT_NULL) {
			where += (name + " ");
			where += value.toString();
		} else if (value instanceof Literal) {
			where += value.toString();
		} else if (value instanceof ResultColumnValue) {
			ResultColumnValue resultColumnValue = (ResultColumnValue) value;
			where += resultColumnValue.process(name, params);
		} else if (value instanceof ExpressionValue) {
			where += (name + " ");
			ExpressionValue expressionValue = (ExpressionValue) value;
			where += expressionValue.getValue();
		} else if (value instanceof GroupCondition) {
			GroupCondition groupCondition = (GroupCondition) value;
			where += groupCondition.process(params);
		} else if (value instanceof Collection<?> && Util.isNotEmpty(value)) {
			// 参数为 Collection的条件（即in, not in条件）不为空
			String marks = " (";

			for (Object o : (Collection<?>) value) {
				marks += "|\1|,";
				params.add(o);
			}

			if (marks.endsWith(",")) {
				marks = marks.substring(0, marks.length() - 1);
			}
			marks += ")"; // marks = " (|\1|,|\1|,|\1|,...,|\1|)"

			// "A in" => "A in (|\1|,|\1|,|\1|,...,|\1|)"
			// "A not in" => "A not in (|\1|,|\1|,|\1|,...,|\1|)"
			where += name;
			where += marks;
		} else {
			where += name;
			where += " |\1|";
			params.add(value);
		}

		return where;
	}

	boolean isValid() {
		if (this.getValue() instanceof Collection<?> && Util.isEmpty(this.getValue())) {
			return false;
		}
		return true;
	}

	protected Condition clone() throws CloneNotSupportedException {
		Condition condition = (Condition) super.clone();
		if (this.value instanceof Collection<?> && Util.isNotEmpty(this.value)) {
			Collection<Object> valueCollection = new ArrayList<Object>();
			valueCollection.addAll((Collection<?>) value);
			condition.value = valueCollection;
		}

		return condition;
	}

}