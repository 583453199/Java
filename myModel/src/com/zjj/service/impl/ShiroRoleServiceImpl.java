package com.zjj.service.impl;

import java.util.List;
import java.util.Map;

import com.zjj.dao.ShiroRoleDao;
import com.zjj.service.ShiroRoleService;
import com.zjj.util.common.Page;

public class ShiroRoleServiceImpl implements ShiroRoleService {

	private ShiroRoleDao shiroRoleDao;

	@Override
	public Map<String, Object> queryRoleList(Map<String, String> requestMap, Page page) {
		return shiroRoleDao.queryRoleList(requestMap, page);
	}

	@Override
	public Map<String, Object> queryRoleByNameOrId(String name, String roleId) {
		return shiroRoleDao.queryRoleByNameOrId(name, roleId);
	}

	@Override
	public boolean addRole(Map<String, Object> paramMap) {
		return shiroRoleDao.addRole(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> queryUserRole(String userId) {
		return shiroRoleDao.queryUserRole(userId);
	}
	
	@Override
	public List<Map<String, Object>> queryExistUserRole(String userId, String status) {
		return shiroRoleDao.queryExistUserRole(userId, status);
	}
	
	@Override
	public boolean updateUserRole(List<String> updateList, List<String> addList, String userId) {
		return shiroRoleDao.updateUserRole(updateList, addList, userId);
	}

	public ShiroRoleDao getShiroRoleDao() {
		return shiroRoleDao;
	}

	public void setShiroRoleDao(ShiroRoleDao shiroRoleDao) {
		this.shiroRoleDao = shiroRoleDao;
	}
}
