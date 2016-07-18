package com.zjj.shiro.util;

import java.util.HashSet;
import java.util.Set;

public class ShiroHelper {

	
	/**
	 * 封装请求参数
	 * 
	 * @param queryString
	 * @return
	 */
	public static Set<String> getParamSet(String queryString) {
		Set<String> paramSet = new HashSet<String>();
		if (queryString == null) {
			return new HashSet<String>();
		}
		String[] allParamArr = queryString.split("&");
		for (String param : allParamArr) {
			paramSet.add(param);
		}
		return paramSet;
	}
}
