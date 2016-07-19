package com.sql.generator;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupCondition extends Condition implements Serializable {
	private static final long serialVersionUID = -2598302627167965722L;
	private List<Condition> conditions = new ArrayList<Condition>();

	/**
	 * 构造函数只有包访问权限
	 */
	GroupCondition(Pref pref) {
		this.pref = pref;
	}

	public GroupCondition append(GroupCondition groupCondition) {
		conditions.add(groupCondition);
		return this;
	}

	public GroupCondition append(String column, Object value) {
		Condition condition = new Condition(column, value);
		conditions.add(condition);
		return this;
	}

	String process(List<Object> params) {
		String where = "";
		Object value = null;
		if (conditions.size() > 0) {
			where += "(";
			Condition cnd = null;
			for (int i = 0; i < conditions.size(); i++) {
				cnd = conditions.get(i);
				if (i > 0) {
					if (this.pref == Pref.AND) {
						where += " and ";
					} else {
						where += " or ";
					}
				}
				if (cnd instanceof GroupCondition) {
					GroupCondition subGroupCondition = (GroupCondition) cnd;
					where += subGroupCondition.process(params);
				} else {
					value = cnd.getValue(); // 目前value只支持最简单形式的几种形式
					String name = cnd.getName();
					if (value instanceof Query) { // 子查询
						Query subQuery = (Query) value;
						Query.initSubQuery(subQuery, false);
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
						where += between.process(cnd.getName(), params);
					} else if (value == Literal.IS_NULL || value == Literal.IS_NOT_NULL) {
						where += (name + " ");
						where += value.toString();
					} else if (value instanceof Literal) {
						where += value.toString();
					} else if (value instanceof ResultColumnValue) {
						ResultColumnValue resultColumnValue = (ResultColumnValue) value;
						where += resultColumnValue.process(name, params);
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
						where += (cnd.getName() + " |\1|");
						params.add(value);
					}
				}
			}
			where += ")";
		}
		return where;
	}
}
