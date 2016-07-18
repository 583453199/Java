package com.zjj.util;

import java.io.Serializable;
//import java.lang.reflect.Field;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import net.sf.json.JsonConfig;
//import net.sf.json.util.CycleDetectionStrategy;
//import net.sf.json.util.JSONUtils;


/**
 * json解析辅助处理类
 * 
 */
public class JsonUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//
//	/**
//	 * 根据不同类型的Java对象获得对应JSON字符串
//	 * 
//	 * @param obj
//	 *            任意Java对象(List或javaBean)
//	 * @return [String]
//	 */
//	public static String getAJsonString(Object obj) throws Exception {
//		return getJsonString(obj, null, null, null);
//	}
//
//	/**
//	 * 根据不同类型的Java对象获得对应JSON字符串
//	 * 
//	 * @param obj
//	 *            任意Java对象(List或javaBean)
//	 * @param excludeFields
//	 *            排除字段
//	 * @param includeFields
//	 *            包含字段
//	 * @return [String]
//	 */
//	public static String getJsonString(Object obj, String[] excludeFields,
//			String[] includeFields) throws Exception {
//		return getJsonString(obj, excludeFields, includeFields, null, false);
//	}
//
//	/**
//	 * 根据不同类型的Java对象获得对应JSON字符串
//	 * 
//	 * @param obj
//	 *            任意Java对象(List或javaBean)
//	 * @param excludeFields
//	 *            排除字段
//	 * @param includeFields
//	 *            包含字段
//	 * @param datePattern
//	 *            日期格式化参数
//	 * @return [String]
//	 */
//	public static String getJsonString(Object obj, String[] excludeFields,
//			String[] includeFields, String datePattern) throws Exception {
//		return getJsonString(obj, excludeFields, includeFields, datePattern,
//				false);
//	}
//
//	/**
//	 * 根据不同类型的Java对象获得对应JSON字符串
//	 * 
//	 * @param obj
//	 *            任意Java对象(List或javaBean)
//	 * @param excludeFields
//	 *            排除字段
//	 * @param includeFields
//	 *            包含字段
//	 * @param datePattern
//	 *            日期格式化参数
//	 * @param escape
//	 *            是否去掉前后的中括号
//	 * @return [String]
//	 */
//	public static String getJsonString(Object obj, String[] excludeFields,
//			String[] includeFields, String datePattern, boolean escape)
//			throws Exception {
//		if (obj == null) {
//		}
//		String json = "";
//		JsonConfig jsonConfig = new JsonConfig();
//		if (includeFields != null && includeFields.length > 0) {
//			jsonConfig = setIncludes(obj, includeFields);
//		} else {
//			jsonConfig = setExcludes(excludeFields);
//		}
//
//		String pattern = "yyyy-MM-dd HH:mm";
//		if (datePattern != null && !"".equals(datePattern)) {
//			pattern = datePattern;
//		}
//		jsonConfig.setIgnoreDefaultExcludes(false);
//		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
//		jsonConfig.registerJsonValueProcessor(Date.class,
//				new JsonDateValueProcessor(pattern));
//		if (obj instanceof List) {
//			json = JSONArray.fromObject(obj, jsonConfig).toString();
//		} else {
//			json = JSONObject.fromObject(obj, jsonConfig).toString();
//		}
//		if (escape) {
//			if (json.startsWith("[")) {
//				json = json.substring(1);
//			}
//			if (json.endsWith("]")) {
//				json = json.substring(0, json.length() - 1);
//			}
//		}
//		return json;
//	}
//
//	/**
//	 * 返回一个JsonConfig对象,包含指定字段
//	 * 
//	 * @param o
//	 *            任意java对象
//	 * @param p
//	 *            包含的字段
//	 */
//	private static JsonConfig setIncludes(Object o, String[] p) {
//		JsonConfig jsonConfig = new JsonConfig();
//		if (p == null || p.length == 0) {// 如果包含字段数组为空则直接返回
//			return jsonConfig;
//		}
//		Set<String> set = new HashSet<String>();
//
//		if (o instanceof List) {// 如果传入的对象是list,包含的字段取list内第一个对象的属性
//			List lst = (List) o;
//			if (lst != null && !lst.isEmpty()) {
//				o = (Object) lst.get(0);
//			} else {
//				return jsonConfig;
//			}
//		}
//
//		if (o instanceof Map) {// 如果是map对象
//			Iterator entries = ((Map) o).entrySet().iterator();
//			while (entries.hasNext()) {
//				Map.Entry entry = (Map.Entry) entries.next();
//				String name = (String) entry.getKey();
//				set.add(name);
//			}
//		} else if (JSONUtils.isObject(o)) {// 如果是javaBean对象
//			Field[] fs = o.getClass().getDeclaredFields();
//			for (Field f : fs) {
//				set.add(f.getName());
//			}
//		}
//		for (String str : p) {
//			set.remove(str);
//		}
//		String[] str = new String[set.size()];
//		jsonConfig.setExcludes(set.toArray(str));
//		return jsonConfig;
//	}
//
//	/**
//	 * 返回一个JsonConfig对象,排除指定字段
//	 * 
//	 * @param o
//	 *            任意java对象
//	 * @param p
//	 *            包含的字段
//	 */
//	private static JsonConfig setExcludes(String[] p) {
//		JsonConfig jsonConfig = new JsonConfig();
//		if (p == null || p.length == 0) {
//			return jsonConfig;
//		}
//		jsonConfig.setExcludes(p);
//		return jsonConfig;
//	}
//
//	// json输出内容转义处理
//	public static String string2Json(String exceptionTrace) {
//
//		if (exceptionTrace.indexOf("'") != -1) {
//			// 将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
//			exceptionTrace = exceptionTrace.replaceAll("'", "\\'");
//		}
//		if (exceptionTrace.indexOf("\"") != -1) {
//			// 将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
//			exceptionTrace = exceptionTrace.replaceAll("\"", "\\\"");
//		}
//
//		if (exceptionTrace.indexOf("\r\n") != -1) {
//			// 将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
//			exceptionTrace = exceptionTrace.replaceAll("\r\n", "");
//		}
//		if (exceptionTrace.indexOf("\n") != -1) {
//			// 将换行转换一下，因为JSON串中字符串不能出现显式的换行
//			exceptionTrace = exceptionTrace.replaceAll("\n", "");
//		}
//
//		return exceptionTrace;
//	}
//
//	public static String parseMsgJson(String resultCode, String resultMsg,
//			String data) {
//		StringBuffer messageBuff = new StringBuffer();
//		// messageBuff.append("{sid:\"" + sid);
//		// messageBuff.append("\",systemCode:\"" + systemCode);
//		messageBuff.append("{resultCode:\"" + resultCode);
//		messageBuff.append("\",resultMsg:\"" + resultMsg);
//		if (data==null || "".equals(data)) {
//			messageBuff.append("\",data:\"\"}");
//		} else {
//			messageBuff.append("\",data:" + data + "}");
//		}
//
//		return messageBuff.toString();
//	}
//
//	public static Object jsonToBean(String jsonStr, Class clazz) {
//		JSONObject jSONObject = JSONObject.fromObject(jsonStr);
//
//		return JSONObject.toBean(jSONObject, clazz);
//
//	}
//	
//	/**
//	 * json 转 list
//	 * 
//	 * @param jsonStr
//	 * @return
//	 */
//	public static List<Map<String, Object>> jsonToList(String jsonStr) {
//		JSONArray jsonArray = JSONArray.fromObject(jsonStr);
//		return (List<Map<String, Object>>) JSONArray.toCollection(jsonArray, Map.class);
//	}
//
//	/**
//	 * json 转 Map
//	 * 
//	 * @param jsonStr
//	 * @return
//	 */
//	public static Map<String, Object> jsonToMap(String jsonStr) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
//		Iterator it = jsonObject.keys();
//		// 遍历jsonObject数据，添加到Map对象
//		while (it.hasNext()) {
//			String key = String.valueOf(it.next());
//			Object value = jsonObject.get(key);
//			data.put(key, value);
//		}
//		return data;
//	}
//
//	public static void main(String args[]) {
//
//		//System.out.println(JsonUtil.jsDesDecrypt("a6b8e9019b53b0", "12345678"));
//	}
}
