package com.sql.generator;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class Generatable<T extends Generatable<?>> implements Serializable {
	private static final long serialVersionUID = 13785188227972854L;

	protected transient String statement = "";

	protected List<Object> params = new ArrayList<Object>();

	protected List<Condition> conditions = new ArrayList<Condition>();

	private String resultCallback;

	public String getResultCallback() {
		return resultCallback;
	}

	public String getCountResultCallback() {
		return resultCallback + Service.KEY_COUNT_CALLBACK_POSIX;
	}

	// limit [offset], rows
	protected int offset;
	protected int rows;

	public T limit(int rows) {
		this.rows = rows;
		return (T) this;
	}

	public T limit(int offset, int rows) {
		this.offset = offset;
		this.rows = rows;
		return (T) this;
	}

	protected boolean provideDefaultLimit; // 只有最外层的query才提供默认limit

	void setProvideDefaultLimit(boolean provideDefaultLimit) {
		this.provideDefaultLimit = provideDefaultLimit;
	}

	// 禁止提供默认limit，优先级最高，用于 ResultColumnValue 和 MergeRule
	private boolean prohibitDefaultLimit = false;

	public boolean isProhibitDefaultLimit() {
		return prohibitDefaultLimit;
	}

	public void setProhibitDefaultLimit(boolean prohibitDefaultLimit) {
		this.prohibitDefaultLimit = prohibitDefaultLimit;
	}

	/**
	 * 指定结果集名称
	 * 
	 * @param resultCallback
	 *            结果集名称
	 */
	public T setResultCallback(String resultCallback) {
		this.resultCallback = resultCallback;
		return (T) this;
	}

	public abstract Command getCommand();

	protected String joinNames(List<Condition> pairs) {
		if (pairs.isEmpty()) {
			return "";
		} else {
			String result = "";
			for (Condition pair : pairs) {
				result += pair.getName() + ",";
			}
			result = result.substring(0, result.length() - 1);
			return result;
		}
	}

	protected String joinParams(List<Condition> pairs) {
		StringBuilder s = new StringBuilder();

		for (int size = pairs.size(), i = 0; i < size; i++) {
			s.append("|\1|");
			s.append(i == size - 1 ? "" : ",");
		}
		return s.toString();
	}

	protected List<Object> joinValues(List<Condition> pairs) {
		if (pairs.isEmpty()) {
			return Collections.emptyList();
		}

		List<Object> result = new ArrayList<Object>();
		for (Condition pair : pairs) {
			result.add(pair.getValue());
		}

		return result;
	}

	public T where(String column, Object value) {
		if (this instanceof Insert) {
			throw new IllegalArgumentException("cannot use 'where' block in Insert");
		}
		addValidCondition(new Condition(column, value));
		return (T) this;
	}

	public T where(boolean conditionMatch, String column, Object value) {
		if (this instanceof Insert) {
			throw new IllegalArgumentException("cannot use 'where' block in Insert");
		}
		if (conditionMatch) {
			addValidCondition(new Condition(column, value));
		}
		return (T) this;
	}

	public T whereIfNotEmpty(String column, Object value) {
		return where(Util.isNotEmpty(value), column, value);
	}

	public T where(ColumnRelation relation) {
		addValidCondition(new Condition(relation));
		return (T) this;
	}

	public T where(GroupCondition groupCondition) {
		addValidCondition(new Condition(groupCondition));
		return (T) this;
	}

	public T append(GroupCondition groupCondition) {
		addValidCondition(new Condition(groupCondition));
		return (T) this;
	}

	public T append(ColumnRelation relation) {
		addValidCondition(new Condition(relation));
		return (T) this;
	}

	public T append(String column, Object value) {
		addValidCondition(new Condition(column, value));
		return (T) this;
	}

	public T append(boolean conditionMatch, String column, Object value) {
		if (conditionMatch) {
			addValidCondition(new Condition(column, value));
		}
		return (T) this;
	}

	public T appendIfNotEmpty(String column, Object value) {
		return append(Util.isNotEmpty(value), column, value);
	}

	public T and(String column, Object value) {
		addValidCondition(new Condition(Pref.AND, column, value));
		return (T) this;
	}

	public T and(boolean conditionMatch, String column, Object value) {
		if (conditionMatch) {
			addValidCondition(new Condition(Pref.AND, column, value));
		}
		return (T) this;
	}

	public T andIfNotEmpty(String column, Object value) {
		return and(Util.isNotEmpty(value), column, value);
	}

	public T and(ColumnRelation relation) {
		addValidCondition(new Condition(Pref.AND, relation));
		return (T) this;
	}

	public T and(GroupCondition groupCondition) {
		addValidCondition(new Condition(Pref.AND, groupCondition));
		return (T) this;
	}

	public T or(String column, Object value) {
		addValidCondition(new Condition(Pref.OR, column, value));
		return (T) this;
	}

	public T or(boolean conditionMatch, String column, Object value) {
		if (conditionMatch) {
			addValidCondition(new Condition(Pref.OR, column, value));
		}
		return (T) this;
	}

	public T orIfNotEmpty(String column, Object value) {
		return or(Util.isNotEmpty(value), column, value);
	}

	public T or(ColumnRelation relation) {
		addValidCondition(new Condition(Pref.OR, relation));
		return (T) this;
	}

	public T or(GroupCondition groupCondition) {
		addValidCondition(new Condition(Pref.OR, groupCondition));
		return (T) this;
	}

	public T append(Literal literal) {
		addValidCondition(new Condition(literal));
		return (T) this;
	}

	private void addValidCondition(Condition condition) {
		// in 语句的参数值为空，不需要in语句
		if (!condition.isValid()) {
			return;
		}
		try {
			Object value = condition.getValue();
			// 有 ResultColumnValue，禁止提供默认limit
			if (value instanceof ResultColumnValue) {
				this.setProhibitDefaultLimit(true);
			}
			this.conditions.add(condition.clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	protected String generateWhereBlock(boolean queryCount) {
		String where = "";

		// 在添加condition和序列化时已经保证值是有效的，<code>Condition.isValid()</code>
		if (this.conditions.isEmpty()) {
			where = "where 1=1";
		} else {
			where = "where";
			for (int i = 0, conditionsSize = conditions.size(); i < conditionsSize; i++) {
				Condition condition = conditions.get(i);
				where = condition.process(i, where, this.params, queryCount);
			}
		}
		return " " + where;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		Iterator<Condition> it = this.conditions.iterator();
		while (it.hasNext()) {
			Condition condition = it.next();
			if (!condition.isValid()) {
				it.remove();
			}
		}
		out.defaultWriteObject();
	}
}