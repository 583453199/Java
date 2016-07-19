package com.zjj.service.impl;

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

	public ShiroRoleDao getShiroRoleDao() {
		return shiroRoleDao;
	}

	public void setShiroRoleDao(ShiroRoleDao shiroRoleDao) {
		this.shiroRoleDao = shiroRoleDao;
	}
}
