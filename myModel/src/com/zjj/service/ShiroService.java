package com.zjj.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zjj.bean.ShiroResourceBean;
import com.zjj.bean.UserBean;


public interface ShiroService {
	
	
	/**
	 * shiro登录验证 
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public String shiroLogin(String account, String password);
	
	/**
	 * 获取所有资源信息
	 * 
	 * @return
	 */
	public List<ShiroResourceBean> queryProtectedResourceList();
	
	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserBean queryUserById(int userId);
	
	/**
	 * 获取用户角色
	 * 
	 * @param userId
	 * @return
	 */
	public Set<String> queryUserRoles(String userId);
	
	/**
	 * 获取角色菜单、按钮资源集合
	 * 
	 * @param roles
	 * @return
	 */
	public Set<String> queryMenuButtonResources(Set<String> roles);
	
	/**
	 * 获取资源对应的所有资源
	 * 
	 * @return
	 */
	public Map<String, Set<String>> queryResourceAndRoleMap();
	
}
