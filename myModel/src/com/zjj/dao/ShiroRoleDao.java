package com.zjj.dao;

import java.util.Map;

import com.zjj.util.common.Page;

public interface ShiroRoleDao {
	
	/**
	 * 查找角色集合
	 * 
	 * @param requestMap
	 * @param page
	 * @return
	 */
	public Map<String, Object> queryRoleList(Map<String, String> requestMap, Page page);
	
	/**
	 * 获取角色信息  唯一性查询
	 * 
	 * @param name
	 * @param roleId
	 * @return
	 */
	public Map<String, Object> queryRoleByNameOrId(String name, String roleId);
	
	/**
	 * 角色新增
	 * 
	 * @param paramMap
	 * @return
	 */
	public boolean addRole(Map<String, Object> paramMap);
}
