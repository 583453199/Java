package com.zjj.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zjj.bean.ShiroResourceBean;
import com.zjj.bean.UserBean;
import com.zjj.dao.ShiroDao;
import com.zjj.dao.UserDao;
import com.zjj.service.ShiroService;

public class ShiroServiceImpl implements ShiroService{
	
	private UserDao userDao;
	
	private ShiroDao shiroDao;
	
	@Override
	public List<ShiroResourceBean> queryProtectedResourceList() {
		return shiroDao.queryProtectedResourceList();
	}

	@Override
	public String shiroLogin(String account, String password) {
		UserBean bean = userDao.queryUserByInfo(account, password);
		if (bean == null) {
			return null;
		}
		return String.valueOf(bean.getUserId());
	}
	
	@Override
	public UserBean queryUserById(int userId) {
		return shiroDao.queryUserById(userId);
	}
	
	@Override
	public Set<String> queryUserRoles(String userId) {
		return shiroDao.queryUserRoles(userId);
	}
	
	@Override
	public Set<String> queryMenuButtonResources(Set<String> roles) {
		return shiroDao.queryMenuButtonResources(roles);
	}
	
	@Override
	public Map<String, Set<String>> queryResourceAndRoleMap() {
		return shiroDao.queryResourceAndRoleMap();
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public ShiroDao getShiroDao() {
		return shiroDao;
	}

	public void setShiroDao(ShiroDao shiroDao) {
		this.shiroDao = shiroDao;
	}
	
}
