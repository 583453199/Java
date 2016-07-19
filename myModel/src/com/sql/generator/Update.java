package com.sql.generator;


import java.util.ArrayList;
import java.util.List;

public class Update extends Generatable<Update> {
	private static final long serialVersionUID = 3105014918176210062L;
	private List<Condition> setValueList = new ArrayList<Condition>();
	private List<Object> tableList = new ArrayList<Object>();
	private String orderBy;
	private boolean checkAffectedRow = true;

	/**
	 * 构造函数只有包访问权限
	 */
	Update() {
	}

	/**
	 * 构造函数只有包访问权限
	 */
	Update(String tables) {
		this.tableList.add(tables);
	}

	public Update update(String table) {
		this.tableList.add(table);
		return this;
	}

	public Update update(Query subQuery) {
		this.tableList.add(subQuery);
		return this;
	}

	public Update set(boolean conditionMatch, String column, Object value) {
		if (conditionMatch) {
			this.setValueList.add(new Condition(column, value));
		}
		return this;
	}

	public Update set(String column, Object value) {
		this.setValueList.add(new Condition(column, value));
		return this;
	}

	public Update setIfNotNull(String column, Object value) {
		return set(value != null, column, value);
	}

	public Update setIfNotEmpty(String column, Object value) {
		return set(Util.isNotEmpty(value), column, value);
	}

	public Update set(ColumnRelation relation) {
		this.setValueList.add(new Condition(relation));
		return this;
	}

	public Update orderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	/**
	 * 调用该方法后，在事务中执行，即使受影响的行数为0，也不回滚事务
	 * 
	 * @return
	 */
	public Update ignoreAffectedRow() {
		this.checkAffectedRow = false;
		return this;
	}

	public Command getCommand() {
		this.params.clear();
		this.statement = "update " + this.generateTableBlock() + generateSetBlock() + generateWhereBlock(false);

		if (Util.isNotEmpty(this.orderBy)) {
			this.statement += " order by " + this.orderBy;
		}

		if (this.rows > 0) {
			String limit = " limit " + this.rows;
			this.statement += limit;
		}
		Command command = new Command(this.statement, this.params);
		if (!this.checkAffectedRow) {
			command.setCheckAffectedRow(false);
		}
		return command;
	}

	private String generateTableBlock() {
		String tableReference = "";
		for (Object table : tableList) {
			if (table instanceof String) {
				tableReference += (table + ",");
			} else if (table instanceof Query) {
				Query subQuery = (Query) table;
				Command command = subQuery.getCommand();
				tableReference += (command.getStatement() + ",");
				for (Object param : command.getParams()) {
					params.add(param);
				}
			}
		}
		if (tableReference.endsWith(",")) {
			tableReference = tableReference.substring(0, tableReference.length() - 1);
		}
		return tableReference;
	}

	private String generateSetBlock() {
		String setStatement = " set ";

		for (int i = 0, num = setValueList.size(); i < num; i++) {
			Condition condition = setValueList.get(i);
			String name = condition.getName();
			Object value = condition.getValue();

			if (value instanceof Query) { // 子查询
				Query subQuery = (Query) value;
				Command command = subQuery.getCommand();
				setStatement += name;
				setStatement += " = (";
				setStatement += command.getStatement();
				setStatement += ")";
				for (Object param : command.getParams()) {
					params.add(param);
				}
			} else if (value instanceof ColumnRelationEquals) {
				ColumnRelationEquals columnRelationEquals = (ColumnRelationEquals) value;
				setStatement += columnRelationEquals.process();
			} else if (value == Literal.IS_NULL || SQL.nullObj().equals(value)) {
				setStatement += name;
				setStatement += " = null";
			} else if (value instanceof ResultColumnValue) {
				ResultColumnValue resultColumnValue = (ResultColumnValue) value;
				setStatement += name;
				setStatement += " = |\1|";
				params.add(resultColumnValue);
			} else if (value instanceof ExpressionValue) {
				setStatement += (name + " = ");
				ExpressionValue expressionValue = (ExpressionValue) value;
				setStatement += expressionValue.getValue();
			} else {
				setStatement += name;
				setStatement += " = |\1|";
				params.add(value);
			}

			if (i < num - 1) {
				setStatement += ",";
			}
		}

		return setStatement;
	}
}