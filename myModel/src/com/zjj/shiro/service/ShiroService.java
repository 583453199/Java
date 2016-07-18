package com.zjj.shiro.service;

import com.zjj.bean.UserBean;
import com.zjj.dao.UserDao;

public class ShiroService {
	
	private UserDao userDao;
	
	/**
	 * shiro登录验证 
	 * @param account
	 * @param password
	 * @return
	 */
	public String shiroLogin(String account, String password) {
		UserBean bean = userDao.queryUserByInfo(account, password);
		if (bean == null) {
			return null;
		}
		return String.valueOf(bean.getUserId());
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	
}
