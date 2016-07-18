package com.sqlgenerator;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Query extends Generatable<Query> implements Serializable {
	private static final long serialVersionUID = -6364846933830384013L;
	public transient static int DEFAULT_QUERY_RECORD_NUM = 10; // 默认查询记录数
	public transient static int MAX_QUERY_RECORD_NUM = 200000; // 最大查询记录数

	// 查询总数的SQL命令，其 SQL 语句形式为 select count(*) as TOTAL_NUM from ...
	// 并且去掉所有的 order by, limit（包括子查询）
	private transient Command countCommand;
	// 默认不查询总数；为true时，不产生 order by, limit 生成 countCommand 使用
	private boolean queryCount = false;

	private boolean generateCountQuery = false;

	boolean isGenerateCountQuery() {
		return generateCountQuery;
	}

	/**
	 * @param generateCountQuery
	 *            true 框架自动生成查询总记录数的sql
	 */
	public Query setGenerateCountQuery(boolean generateCountQuery) {
		this.generateCountQuery = generateCountQuery;
		return this;
	}

	private boolean isOutermost = false; // 默认不是最外层查询语句

	private String alias; // 查询自身的别名

	private String groupAlias; // join 或者 union 后的别名

	private List<Object> columnList = new ArrayList<Object>();

	private List<Object> fromList = new ArrayList<Object>();

	private String orderBy;

	private String groupBy;

	private String having;

	private List<Object> leftJoinList = new ArrayList<Object>();

	private List<UnionQuery> unionQueryList = new ArrayList<UnionQuery>();

	/**
	 * 构造函数只有包访问权限
	 */
	Query(String columns) {
		this.columnList.add(columns);
	}

	public Query select(Query subQuery) {
		this.columnList.add(subQuery);
		return this;
	}

	public Query select(String columns) {
		this.columnList.add(columns);
		return this;
	}

	public Query from(String from) {
		this.fromList.add(from);
		return this;
	}

	public Query from(Query query) {
		this.fromList.add(query);
		return this;
	}

	public Query leftJoin(String leftJoin) {
		this.leftJoinList.add(leftJoin);
		return this;
	}

	public Query leftJoin(LeftJoin leftJoin) {
		this.leftJoinList.add(leftJoin);
		return this;
	}

	public Query union(Query query) {
		this.unionQueryList.add(new UnionQuery(query, false));
		return this;
	}

	public Query unionAll(Query query) {
		this.unionQueryList.add(new UnionQuery(query, true));
		return this;
	}

	public Query orderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Query groupBy(String groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public Query having(String having) {
		this.having = having;
		return this;
	}

	public Query as(String alias) {
		this.alias = alias;
		return this;
	}

	public Query as() {
		this.alias = "";
		return this;
	}

	public Query setGroupAlias(String groupAlias) {
		this.groupAlias = groupAlias;
		return this;
	}

	public Command getCommand() {
		this.params.clear();
		this.statement = "";

		// 处理包含group by 的总数查询，采用
		// select count(*) as TOTAL_NUM from
		// (select count(*) as TOTAL_NUM from ... group by ..) as a
		// 不采用 select count(distinct ..) as TOTAL_NUM from ..., 是由于处理不了 group by ... having
		boolean selectTotalNum = queryCount && isOutermost;
		if (selectTotalNum && Util.isNotEmpty(this.groupBy)) {
			this.statement += "select count(*) as TOTAL_NUM from (";
		}
		
		if (this.groupAlias != null) {
			this.statement += "(";
		}
		if (this.alias != null) {
			this.statement += "(";
		}
		
		this.statement += "select ";
		this.statement += (selectTotalNum ? "count(*) as TOTAL_NUM" : this.generateColumnBlock());
		this.statement += (" from " + this.generateFromBlock());

		for (Object obj : this.leftJoinList) {
			if (obj instanceof String) {
				this.statement += " left join " + obj;
			} else if (obj instanceof LeftJoin) {
				LeftJoin leftJoin = (LeftJoin) obj;
				Query leftJoinQuery = leftJoin.getSubQuery();
				initSubQuery(leftJoinQuery);
				String on = leftJoin.getOn();
				String alias = leftJoin.getAlias();

				this.statement += " left join (";

				Command command = leftJoinQuery.getCommand();
				this.statement += command.getStatement();
				for (Object param : command.getParams()) {
					params.add(param);
				}
				this.statement += ")";

				if (alias != null) {
					this.statement += " as " + alias;
				}
				this.statement += (" on " + on);
			}
		}

		this.statement += generateWhereBlock(queryCount);

		if (Util.isNotEmpty(this.groupBy)) {
			this.statement += " group by " + this.groupBy;
		}

		if (Util.isNotEmpty(this.having)) {
			this.statement += " having " + this.having;
		}

		if (Util.isNotEmpty(this.orderBy) && !queryCount) {
			this.statement += " order by " + this.orderBy;
		}

		if (this.rows < 0 || this.rows > MAX_QUERY_RECORD_NUM) {
			this.rows = MAX_QUERY_RECORD_NUM;
		} else if (this.rows == 0 && this.isProhibitDefaultLimit() == false && this.provideDefaultLimit) {
			this.rows = DEFAULT_QUERY_RECORD_NUM;
		}

		if (!this.queryCount && this.rows > 0) {
			String limit = " limit " + (this.offset > 0 ? this.offset + "," + this.rows : this.rows);
			this.statement += limit;
		}

		if (this.alias != null) {
			if (Util.isEmpty(this.alias)) {
				this.statement += ")";
			} else {
				this.statement += ") as " + this.alias;
			}
		}

		for (UnionQuery unionQuery : this.unionQueryList) {
			Query query = unionQuery.getQuery();
			initSubQuery(query);
			Command command = query.getCommand();
			this.statement += unionQuery.isUnionAll() ? " union all " : " union ";
			this.statement += command.getStatement();
			if (command.getParams() != null) {
				this.params.addAll(command.getParams());
			}
		}

		if (this.groupAlias != null) {
			if (Util.isEmpty(this.groupAlias)) {
				this.statement += ")";
			} else {
				this.statement += ") as " + this.groupAlias;
			}
		}
		
		if (selectTotalNum && Util.isNotEmpty(this.groupBy)) {
			this.statement += ") as a";
		}

		return new Command(this.statement, this.params);
	}

	private String generateColumnBlock() {
		String columnBlock = "";
		for (Object column : columnList) {
			if (column instanceof String) {
				columnBlock += (column + ",");
			} else if (column instanceof Query) {
				Query query = (Query) column;
				initSubQuery(query);
				Command command = query.getCommand();
				columnBlock += (command.getStatement() + ",");
				for (Object param : command.getParams()) {
					params.add(param);
				}
			}
		}
		if (columnBlock.endsWith(",")) {
			columnBlock = columnBlock.substring(0, columnBlock.length() - 1);
		}
		return columnBlock;
	}

	private void initSubQuery(Query query) {
		if (queryCount) {
			query.isOutermost = false;
			query.queryCount = true;
		}
	}

	static void initSubQuery(Query query, boolean queryCount) {
		if (queryCount) {
			query.isOutermost = false;
			query.queryCount = true;
		}
	}

	private String generateFromBlock() {
		String fromBlock = "";
		for (Object from : fromList) {
			if (from instanceof String) {
				fromBlock += (from + ",");
			} else if (from instanceof Query) {
				Query subQuery = (Query) from;
				initSubQuery(subQuery);
				Command command = subQuery.getCommand();
				fromBlock += (command.getStatement() + ",");
				for (Object param : command.getParams()) {
					params.add(param);
				}
			}
		}
		if (fromBlock.endsWith(",")) {
			fromBlock = fromBlock.substring(0, fromBlock.length() - 1);
		}
		return fromBlock;
	}

	public String toString() {
		return getCommand().toString();
	}

	public String getAlias() {
		return this.alias;
	}

	/**
	 * @return 查询总数的SQL命令
	 */
	public Command getCountCommand() {
		if (countCommand == null) {
			this.statement = "";
			this.params.clear();
			this.queryCount = true;
			this.isOutermost = true;
			countCommand = getCommand();
		}
		return countCommand;
	}

}