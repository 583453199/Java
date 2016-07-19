package com.zjj.util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sql.generator.Command;
import com.sql.generator.MergeRule;
import com.sql.generator.ResultColumnValue;
import com.sql.generator.SQL;
import com.sql.generator.Service;
import com.sql.generator.Util;


public class HibernateBasic extends HibernateDaoSupport {

	private final Log LOG = LogFactory.getLog(HibernateBasic.class);
	public static final String KEY_SQL_BUFFER = "_SQL";
	public static final int LOG_SELECT_CHAR_THRESHOLD = 1024;
	
	private String statement;
	private Map<String, Object> responseDataMap;
	private List<Object> params;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> execute(Service service) {
		responseDataMap = new LinkedHashMap<String, Object>();
		Map requestMap = service.getRequestMap();
		requestMap = (Map) requestMap.get(Service.KEY_TRANSFER_SERVICE);

		boolean useTransaction = (Boolean) requestMap.get(Service.KEY_TRANSFER_USE_TRANSACTION);
		boolean useCustomResultTransformer = (Boolean) requestMap.get(Service.KEY_TRANSFER_USE_CUSTOM_RESULTTRANSFORMER);
		List<Command> commandList = (List<Command>) requestMap.get(Service.KEY_TRANSFER_SERVICE_DATA);
		Session session = getSessionFactory().openSession();
		Transaction transaction = null; 
		if (useTransaction) {
			transaction = session.beginTransaction();
			transaction.begin();
		}
		for (Command command : commandList) {
			statement = command.getStatement();
			params = command.getParams();
			String resultCallback = command.getResultCallback();
			boolean checkAffectedRow = command.isCheckAffectedRow();

			this.executeCommand(session, responseDataMap, resultCallback, useTransaction, useCustomResultTransformer, checkAffectedRow);
			if (responseDataMap.get(Util.KEY_ERROR) != null) {
				responseDataMap.put(CommonUtil.RET_STATUS, "F");
				return responseDataMap;
			}
		}
		if (useTransaction) {
			transaction.commit();
		}
		session.close();
		List<Map<String, Object>> mergeRuleList = (List<Map<String, Object>>) requestMap
				.get(Service.KEY_TRANSFER_MERGE_RULE);
		for (Map<String, Object> mergeRule : mergeRuleList) { // 合并结果集
			merge(responseDataMap, mergeRule);
		}

		for (Map<String, Object> mergeRule : mergeRuleList) { // 被合并的结果集不单独显示
			String merge = (String) mergeRule.get(MergeRule.KEY_MERGE);
			responseDataMap.remove(merge);
		}
		responseDataMap.put(CommonUtil.RET_STATUS, "S");
		return responseDataMap;
	}

	@SuppressWarnings("unchecked")
	public void executeCommand(Session session, Map<String, Object> responseDataMap,
			String resultCallback, boolean useTransaction, boolean useCustomResultTransformer, boolean checkAffectedRow) {
		loadParamsInfo(responseDataMap);
		
		String sqlWithValue = statement;
		statement = statement.replace(Util.PARAM_PLACEHOLDER, "?");
		Query query = session.createSQLQuery(statement);
		boolean singleQuote = true;
		for (int i = 0; i < params.size(); i++) {
			singleQuote = true; // 输出sql日志时，默认为参数值加单引号
			Object param = params.get(i);
			query.setParameter(i, param);
			// 为 null和Number时，不加单引号
			if (param == null || param instanceof Number) {
				singleQuote = false;
			}
			sqlWithValue = Util.getSqlWithValue(sqlWithValue, param, singleQuote);
		}

		if (useCustomResultTransformer) {
			query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		} else {
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		}

		logSql(sqlWithValue, null);
		Object obj = null;
		boolean isQuery = statement.startsWith("select");
		boolean isUpdate = statement.startsWith("update");
		boolean isInsert = statement.startsWith("insert");
		boolean execSuccess = true;
		try {
			if (isQuery) {
				obj = query.list();
			} else {
				obj = query.executeUpdate();
			}
		} catch (Exception e) {
			execSuccess = false;
			LOG.error("执行数据库操作失败：" + sqlWithValue, e);
			responseDataMap.put(Util.KEY_ERROR, "\n执行数据库操作失败: " + e.getMessage() + "\n" + e.getCause());
		}
		
		Map<String, Object> executeResultForNotQuery = new HashMap<String, Object>();
		// 设置执行结果
		if (isQuery) {
			responseDataMap.put(resultCallback, obj);
		} else {
			responseDataMap.put(resultCallback, executeResultForNotQuery);
			executeResultForNotQuery.put(Service.KEY_TRANSFER_AFFECTED_ROW, obj); // 受影响的行数
		}
		
		if (isInsert) { // 获取 last_insert_id
			Query lastInsertIdQuery = session.createSQLQuery("select last_insert_id()");
			List<BigInteger> lastInsertIdList = lastInsertIdQuery.list();
			BigInteger lastInsertId = lastInsertIdList.get(0);
			executeResultForNotQuery.put(ResultColumnValue.KEY_LAST_INSERT_ID, lastInsertId);
		} else if (isUpdate && useTransaction && checkAffectedRow && execSuccess) {
			// 如果事务中的update语句执行后，受影响的行数为0，则需要回滚事务
			int affectedRow = (Integer) obj;
			if (affectedRow <= 0) {
				String errorMsg = "\n事务中的update语句执行后，受影响的行数为0，回滚事务";
				responseDataMap.put(Util.KEY_ERROR, errorMsg);
				responseDataMap.put(Util.KEY_ERROR_CODE, Util.ERROR_CODE_ROLLBACK_NOT_UPDATE_ROW);
				logSql("执行失败 ", sqlWithValue, errorMsg);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void merge(Map<String, Object> responseDataMap, Map<String, Object> mergeRule) {
		String into = (String) mergeRule.get(MergeRule.KEY_INTO);
		List<Map<String, Object>> intoResultList = (List<Map<String, Object>>) responseDataMap.get(into);
		if (intoResultList == null || intoResultList.size() == 0) {
			return;
		}

		String merge = (String) mergeRule.get(MergeRule.KEY_MERGE);
		String mergeCol = (String) mergeRule.get(MergeRule.KEY_MERGECOL);
		String intoCol = (String) mergeRule.get(MergeRule.KEY_INTOCOL);
		boolean oneToMany = (Boolean) mergeRule.get(MergeRule.KEY_ONE_TO_MANY);

		List<Map<String, Object>> mergeResultList = (List<Map<String, Object>>) responseDataMap.get(merge);

		for (Map<String, Object> intoResult : intoResultList) {
			Object matchValue = intoResult.get(intoCol);
			if (matchValue == null) {
				continue;
			}
			if (oneToMany) {
				List<Map<String, Object>> mergedList = getMergedList(matchValue, mergeCol, mergeResultList);
				intoResult.put(merge, mergedList);
			} else {
				Map<String, Object> mergedMap = getMergedMap(matchValue, mergeCol, mergeResultList);
				for (Map.Entry<String, Object> entry : mergedMap.entrySet()) {
					intoResult.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	private List<Map<String, Object>> getMergedList(Object matchValue, String mergeCol,
			List<Map<String, Object>> mergedResultList) {
		List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
		for (Map<String, Object> mergedResult : mergedResultList) {
			if (matchValue.equals(mergedResult.get(mergeCol))) {
				result.add(mergedResult);
			}
		}
		return result;
	}

	private Map<String, Object> getMergedMap(Object matchValue, String mergeCol,
			List<Map<String, Object>> mergedResultList) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		for (Map<String, Object> mergedResult : mergedResultList) {
			if (matchValue.equals(mergedResult.get(mergeCol))) {
				return mergedResult;
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private void loadParamsInfo(Map<String, Object> responseDataMap) {
		List<Object> resolvedParams = new LinkedList<Object>();
		// 随着参数值直接写入到statement中，placeholder的总数量会变化
		int placeholderOffset = 1;
		for (int i = 0; i < this.params.size(); i++) {
			Object param = this.params.get(i);
			if (param instanceof ResultColumnValue) {
				ResultColumnValue resultColumn = (ResultColumnValue) param;
				String keyColumnName = resultColumn.getColumnName();
				Object obj = responseDataMap.get(resultColumn.getResultCallback());
				
				List<Map<String, Object>> queryResultList = (List<Map<String, Object>>) obj;
				Object defaultValue = resultColumn.getDefaultValue();
				if (queryResultList == null || queryResultList.size() < 1) {
					if (defaultValue == null) { // 少于1行记录 && 没有默认值，报错
						System.out.println("少于1行记录 && 没有默认值" + defaultValue);
						break;
					}
					if (SQL.nullObj().equals(defaultValue)) { // 自定义的null对象
						defaultValue = null;
					}
					// 除了null和Number，都加单引号
					if ((defaultValue == null || defaultValue instanceof Number) == false) {
						defaultValue = ("'" + defaultValue + "'");
					}
					param = "(" + defaultValue + ")";
				} else {
					StringBuilder buf = new StringBuilder("(");
					for (int j = 0; j < queryResultList.size(); j++) {
						Map<String, Object> resultMap = queryResultList.get(j);
						if (!resultMap.containsKey(keyColumnName)) {
							System.out.println("查询记录中没有列名：" + keyColumnName);
							break;
						}
						if (j > 0) {
							buf.append(",");
						}
						Object columnValue = resultMap.get(keyColumnName);
						// 除了null和Number，都加单引号
						if ((columnValue == null || columnValue instanceof Number) == false) {
							columnValue = ("'" + columnValue + "'");
						}
						buf.append(columnValue);
					}
					buf.append(")");
					param = buf.toString();
				}
			} else if (param instanceof Map == false) {
				resolvedParams.add(param);
				placeholderOffset++;
				continue;
			} 
			// 把参数直接写到statement中，不再作为sql的参数
			this.statement = Util.getSqlWithValue(this.statement, param, placeholderOffset, false);
		}
		this.params = resolvedParams;
	}
	
	private void logSql(String prefix, String sqlWithValue, String errorMsg) {
//		if (LOG.isInfoEnabled()) {}
		StringBuilder buf = new StringBuilder();
		if (prefix != null) {
			buf.append(prefix);
		}
		// 查询语句
		if (sqlWithValue.length() > LOG_SELECT_CHAR_THRESHOLD && sqlWithValue.startsWith("select")) {
			sqlWithValue = sqlWithValue.substring(0, LOG_SELECT_CHAR_THRESHOLD) + "...";
		}
		buf.append("sql=" + sqlWithValue + ";params=" + this.params);
		if (errorMsg != null) {
			buf.append(errorMsg);
		}
		StringBuilder sqlBuf = (StringBuilder) responseDataMap.get(KEY_SQL_BUFFER);
		if (sqlBuf == null) {
			this.responseDataMap.put(KEY_SQL_BUFFER, buf);
		} else {
			sqlBuf.append("\n");
			sqlBuf.append(buf.toString());
		}
		if (buf != null) {
			System.out.println(buf.toString());
		}
	}

	private void logSql(String sqlWithValue, String errorMsg) {
		logSql(null, sqlWithValue, errorMsg);
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}

	public Map<String, Object> getResponseDataMap() {
		return responseDataMap;
	}

	public void setResponseDataMap(Map<String, Object> responseDataMap) {
		this.responseDataMap = responseDataMap;
	}
	
}
