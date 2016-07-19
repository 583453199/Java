package com.sql.generator;


/**
 * sql语句生成器
 * 
 */
public class SQL {
	private SQL() {
	}

	public static Query select(String columns) {
		return new Query(columns);
	}

	public static Update update(Query subQuery) {
		return new Update().update(subQuery);
	}

	public static Update update(String table) {
		return new Update(table);
	}

	public static Insert insert(String table) {
		return new Insert(table);
	}

	public static LeftJoin leftJoin(Query subQuery, String on) {
		return new LeftJoin(subQuery, on);
	}

	public static LeftJoin leftJoin(Query subQuery, String on, String alias) {
		return new LeftJoin(subQuery, on, alias);
	}

	/**
	 * @param leftColumn
	 *            只允许[a-zA-Z0-9_.]范围内的字符
	 * @param relation
	 * @param rightColumn
	 *            只允许[a-zA-Z0-9_.]范围内的字符
	 * @return
	 */
	public static ColumnRelation columnRelation(String leftColumn, Relation relation, String rightColumn) {
		if (leftColumn.matches("[a-zA-Z0-9_.]+") == false) {
			throw new IllegalArgumentException("只允许[a-zA-Z0-9_.]范围内的字符, leftColumn=" + leftColumn);
		}
		if (rightColumn.matches("[a-zA-Z0-9_.]+") == false) {
			throw new IllegalArgumentException("只允许[a-zA-Z0-9_.]范围内的字符, rightColumn=" + rightColumn);
		}
		if (relation == Relation.EQUALS) {
			return new ColumnRelationEquals(leftColumn, rightColumn);
		}
		return new ColumnRelation(leftColumn, relation, rightColumn);
	}

	public static ColumnRelation columnRelationEquals(String leftColumn, String rightColumn) {
		return columnRelation(leftColumn, Relation.EQUALS, rightColumn);
	}

	public static Between between(Object min, Object max) {
		return new Between(min, max, false);
	}

	public static Between between(Object min, Object max, boolean isNot) {
		return new Between(min, max, isNot);
	}

	/**
	 * 获取Insert中last_insert_id()返回的值
	 */
	public static ResultColumnValue lastInsertId(Insert insert) {
		return new ResultColumnValue(insert, ResultColumnValue.KEY_LAST_INSERT_ID, null);
	}

	/**
	 * 获取Query中columnName返回的值
	 * 
	 * @param query
	 * @param columnName
	 * @param defaultValue
	 *            如果没有获取到1行记录，该值不为null，使用该值; 如果没有获取到1行记录，该值为null，则报错，查询失败
	 * @return
	 */
	public static ResultColumnValue resultColumnValue(Query query, String columnName, Object defaultValue) {
		return new ResultColumnValue(query, columnName, defaultValue);
	}

	/**
	 * <p>
	 * <li>
	 * 获取Query中columnName返回的值, 如果没有获取到1行记录，则报错，查询失败；
	 * <p>
	 * <li>
	 * 用于必须要获取到值的情况；
	 * <p>
	 * <li>
	 * 如果不一定获取到值，使用可以指定默认值的另外一个函数
	 * 
	 * @param query
	 * @param columnName
	 * @return
	 */
	public static ResultColumnValue resultColumnValue(Query query, String columnName) {
		return new ResultColumnValue(query, columnName, null);
	}

	public static GroupCondition or(String[] columns, Object[] values) {
		GroupCondition groupCondition = new GroupCondition(Pref.OR);
		for (int j = 0; j < columns.length; j++) {
			groupCondition.append(columns[j], values[j]);
		}
		return groupCondition;
	}

	/**
	 * 只是用于一个列等于多个值中的某一个的情况
	 * 
	 * @param column
	 *            比如 IS_SHOW_TEACHER
	 * @param values
	 *            {Literal.IS_NULL, 0}
	 * @return
	 */
	public static GroupCondition or(String column, Object[] values) {
		String[] columns = new String[values.length];
		for (int i = 0; i < columns.length; i++) {
			if (values[i] == Literal.IS_NULL || values[i] == Literal.IS_NOT_NULL) {
				columns[i] = column;
			} else {
				columns[i] = column + " =";
			}
		}
		return or(columns, values);
	}

	/**
	 * 合并才用的是oneToOne模式，另外一种模式是oneToMany
	 * 
	 * @param merge
	 * @param into
	 * @param mergeCol
	 * @param intoCol
	 * @return
	 */
	public static MergeRule mergeRule(Generatable<?> merge, Generatable<?> into, String mergeCol, String intoCol) {
		return new MergeRule(merge, into, mergeCol, intoCol, false);
	}

	public static MergeRule mergeRule(Generatable<?> merge, Generatable<?> into, String mergeCol, String intoCol,
			boolean oneToMany) {
		return new MergeRule(merge, into, mergeCol, intoCol, oneToMany);
	}

	public static GroupCondition groupCondition(Pref pref) {
		GroupCondition groupCondition = new GroupCondition(pref);
		return groupCondition;
	}

	public static String nullObj() {
		return Util.PARAM_NULL;
	}

	public static ExpressionValue expressionValue(String value) {
		return new ExpressionValue(value);
	}

}
