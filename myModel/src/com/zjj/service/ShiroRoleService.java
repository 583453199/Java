package com.zjj.service;

import java.util.List;
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

	/**
	 * 查询用户角色 -授权
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> queryUserRole(String userId);
	
	/**
	 * 只获取t_user_role表存在的角色信息
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	public List<Map<String, Object>> queryExistUserRole(String userId, String status);

	/**
	 * 修改用户角色授权关系
	 * 
	 * @param updateList
	 * @param addList
	 * @param userId
	 * @return
	 */
	public boolean updateUserRole(List<String> updateList, List<String> addList, String userId);
}
