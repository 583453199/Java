package com.zjj.dao;

import java.util.List;
import java.util.Map;

import com.zjj.bean.ShiroResourceBean;
import com.zjj.util.common.Page;

public interface ShiroResourceDao {
	
	/**
	 * 新增资源
	 * 
	 * @param bean
	 * @return
	 */
	public String addShiroResource(ShiroResourceBean bean);
	
	/**
	 * 查询资源列表
	 * 
	 * @param requestMap
	 * @param page
	 * @return
	 */
	public Map<String, Object> queryResourceList(Map<String, String> requestMap, Page page);
	
	/**
	 * 获取角色授权资源
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Map<String, Object>> queryRoleResourceList(String roleId);
	
	/**
	 * 修改角色授权状态为删除
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updatePermissionByRoleId(String roleId);
	
	/**
	 * 获取角色授权资源信息
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Map<String, Object>> queryPermissionByRoleId(String roleId);

	/**
	 * 角色 - 资源授权
	 * 
	 * @param updateList
	 * @param addList
	 * @param roleId
	 * @return
	 */
	public boolean updatePermissionInfo(List<String> updateList, List<String> addList, String roleId);
	
	/**
	 * 获取第N级中的所有资源对象
	 * 
	 * @param level
	 * @param prefix
	 * @return
	 */
	public List<Map<String, Object>> queryAllResourceByLevel(int level, String prefix);
	
	/**
	 * 获取资源信息 
	 * 
	 * @param type
	 * @param status
	 * @return
	 */
	public List<Map<String, Object>> queryResourceType(int type, String status);
}
