package com.sqlgenerator;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表示 SQL 命令的类。其中包含 SQL 语句和参数两个部分。参数的值和 SQL 语句中的问号一一对应。
 */
public class Command implements Serializable, Cloneable {
	private static final long serialVersionUID = -2275552230580946242L;
	public transient static final String KEY_STATEMENT = "statement";
	public transient static final String KEY_PARAMS = "params";
	public transient static final String KEY_RESULTCALLBACK = "resultCallback";
	public transient static final String KEY_CHECKAFFECTEDROW = "checkAffectedRow";

	private String statement;
	private List<Object> params;
	private String resultCallback;
	private boolean checkAffectedRow = true;

	/**
	 * 构造函数只有包访问权限
	 */
	Command() {
	}

	/**
	 * 构造函数只有包访问权限
	 * 
	 * @param statement
	 *            SQL 语句
	 * @param params
	 *            参数
	 */
	Command(String statement, List<Object> params) {
		this.statement = statement;
		this.params = params;
	}

	/**
	 * 转成json时候，需要get方法为public，否则报 No serializer found for class
	 * 
	 * @return SQL 语句
	 */
	public String getStatement() {
		return statement;
	}

	/**
	 * 转成json时候，需要get方法为public，否则报 No serializer found for class
	 * 
	 * @return 参数
	 */
	public List<Object> getParams() {
		return params;
	}

	/**
	 * 转成json时候，需要get方法为public，否则报 No serializer found for class
	 * 
	 * @return 结果集名称
	 */
	public String getResultCallback() {
		return resultCallback;
	}

	void setResultCallback(String resultCallback) {
		this.resultCallback = resultCallback;
	}

	public boolean isCheckAffectedRow() {
		return checkAffectedRow;
	}

	public void setCheckAffectedRow(boolean checkAffectedRow) {
		this.checkAffectedRow = checkAffectedRow;
	}

	public String toString() {
		return "Command{" + KEY_STATEMENT + "=" + statement + ", " + KEY_PARAMS + "=" + params + ", "
				+ KEY_RESULTCALLBACK + "=" + resultCallback + ", " + KEY_CHECKAFFECTEDROW + "=" + checkAffectedRow
				+ "}";
	}

	public Command clone() {
		List<Object> cloneParams = new ArrayList<Object>();
		cloneParams.addAll(this.params);
		Command command = new Command(this.statement, this.params);
		return command;
	}
}
