package com.zjj.service;

import java.util.Map;

import com.zjj.util.common.Page;

public interface ShiroRoleService {
	
	/**
	 * 查找角色集合
	 * 
	 * @param requestMap
	 * @param page
	 * @return
	 */
	public Map<String, Object> queryRoleList(Map<String, String> requestMap, Page page);
}
