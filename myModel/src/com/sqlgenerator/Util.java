package com.sqlgenerator;


import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Util {
	public static final String PARAM_PLACEHOLDER = "|\1|";
	// null对象，目前用于ResultColumnValue.defaultValue
	public static final String PARAM_NULL = "|\2|";
	private static final String SYS_HEAD = "SYS_HEAD";
	private static final String RET_STATUS = "RET_STATUS";
	// 事务中的update语句执行后，受影响的行数为0，回滚事务
	public static final String ERROR_CODE_ROLLBACK_NOT_UPDATE_ROW = "1";
	public static final String KEY_ERROR = "ERROR";
	public static final String KEY_ERROR_CODE = "ERROR_CODE";

	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}

		if (obj instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) obj;
			return collection.size() == 0;
		}

		if (obj instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) obj;
			return map.size() == 0;
		}

		String str = obj.toString();
		return str.length() == 0 || str.trim().length() == 0;
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static String getSqlWithValue(String sqlWithPlaceholder, Object param, boolean singleQuote) {
		return getSqlWithValue(sqlWithPlaceholder, param, 1, singleQuote);
	}

	/**
	 * @param sqlWithPlaceholder
	 * @param param
	 * @param placeholderOffset
	 *            替换第几个placeholder, 从1开始
	 * @param singleQuote
	 *            true 参数值前后添加单引号
	 * @return
	 */
	public static String getSqlWithValue(String sqlWithPlaceholder, Object param, int placeholderOffset,
			boolean singleQuote) {
		int i = sqlWithPlaceholder.indexOf(PARAM_PLACEHOLDER);
		if (i == -1) {
			return sqlWithPlaceholder;
		}
		while (placeholderOffset > 1) {
			i = sqlWithPlaceholder.indexOf(PARAM_PLACEHOLDER, i + PARAM_PLACEHOLDER.length());
			if (i == -1) {
				return sqlWithPlaceholder;
			}
			placeholderOffset--;
		}

		if (singleQuote) {
			param = "'" + (param + "").replace("'", "''") + "'";
		}

		return sqlWithPlaceholder.substring(0, i) + param
				+ sqlWithPlaceholder.substring(i + PARAM_PLACEHOLDER.length());
	}

	/**
	 * @param result
	 *            格式类似
	 * 
	 *            <pre>
	 * {
	 *     SYS_HEAD={
	 *         RET_STATUS=F  # or RET_STATUS=S
	 *     },
	 *     a8d0953a-cf34-4b7d-be47-2a5d82ae5350={
	 *         _lastInsertId=339,
	 *         _affectedRow=1
	 *     },
	 *     _rn_1=[
	 *         {score:30, user_id:1}
	 *     ],
	 *     _rn_1_count=[
	 *         {TOTAL_NUM: 1}
	 *     ]
	 * }
	 * </pre>
	 * @return
	 */
	public static final long getTotalNum(Map<String, Object> result, Query query) {
		String num = getTotalNumStr(result, query);
		return Long.parseLong(num);
	}

	@SuppressWarnings("unchecked")
	public static final String getTotalNumStr(Map<String, Object> result, Query query) {
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get(query.getCountResultCallback());
		Object obj = resultList.get(0).get(Service.TOTAL_NUM);
		if (obj instanceof BigInteger) {
			BigInteger bi = (BigInteger) obj;
			return bi.longValue() + "";
		} else {
			String num = (String) obj;
			return num;
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean isInvalid(Map<String, Object> result) {
		if (result == null) {
			return true;
		}
		Map sysHead = (Map) result.get(SYS_HEAD);
		if (sysHead == null) {
			return true;
		}
		String status = (String) sysHead.get(RET_STATUS);

		return !"S".equalsIgnoreCase(status);
	}

	public static String trim(String content) {
		return content == null ? null : content.trim();
	}

	/**
	 * @param result
	 *            如果事务中的update语句执行后，受影响的行数为0，回滚事务， 返回true，否则返还false
	 * @return
	 */
	public static boolean isRollackForNotUpdateRow(Map<String, Object> result) {
		if (result == null) {
			return false;
		}
		Object errorCode = result.get(KEY_ERROR_CODE);
		return ERROR_CODE_ROLLBACK_NOT_UPDATE_ROW.equals(errorCode);
	}
}