package com.zjj.service;

import java.util.List;
import java.util.Map;

import com.zjj.bean.ShiroResourceBean;
import com.zjj.util.common.Page;

public interface ShiroResourceService {

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
	 * 
	 * 
	 * @param updateList
	 * @param addList
	 * @param roleId
	 * @return
	 */
	public boolean updatePermissionInfo(List<String> updateList, List<String> addList, String roleId);
}
