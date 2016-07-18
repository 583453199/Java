package com.zjj.service.impl;

import java.util.List;
import java.util.Map;

import com.zjj.bean.ShiroResourceBean;
import com.zjj.dao.ShiroResourceDao;
import com.zjj.service.ShiroResourceService;
import com.zjj.util.common.Page;

public class ShiroResourceServiceImpl implements ShiroResourceService{
	
	private ShiroResourceDao shiroResourceDao;
	

	@Override
	public String addShiroResource(ShiroResourceBean bean) {
		return shiroResourceDao.addShiroResource(bean);
	}
	
	@Override
	public Map<String, Object> queryResourceList(Map<String, String> requestMap, Page page) {
		return shiroResourceDao.queryResourceList(requestMap, page);
	}
	
	@Override
	public List<Map<String, Object>> queryRoleResourceList(String roleId) {
		return shiroResourceDao.queryRoleResourceList(roleId);
	}
	
	@Override
	public boolean updatePermissionByRoleId(String roleId) {
		return shiroResourceDao.updatePermissionByRoleId(roleId);
	}
	
	@Override
	public boolean updatePermissionInfo(List<String> updateList, List<String> addList, String roleId) {
		return shiroResourceDao.updatePermissionInfo(updateList, addList, roleId);
	}
	
	public List<Map<String, Object>> queryPermissionByRoleId(String roleId) {
		return shiroResourceDao.queryPermissionByRoleId(roleId);
	}

	public ShiroResourceDao getShiroResourceDao() {
		return shiroResourceDao;
	}

	public void setShiroResourceDao(ShiroResourceDao shiroResourceDao) {
		this.shiroResourceDao = shiroResourceDao;
	}

}
