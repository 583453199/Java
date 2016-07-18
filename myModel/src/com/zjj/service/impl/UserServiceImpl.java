package com.zjj.service.impl;

import java.util.Map;

import com.zjj.bean.UserBean;
import com.zjj.dao.UserDao;
import com.zjj.service.UserService;
import com.zjj.util.common.Page;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	@Override
	public boolean login(String account, String password) {
		return userDao.login(account, password);
	}
	
	@Override
	public UserBean queryUserByInfo(String account, String password) {
		return userDao.queryUserByInfo(account, password);
	}
	
	@Override
	public String addUser(UserBean userBean) {
		return userDao.addUser(userBean);
	}
	
	@Override
	public Map<String, Object> queryUserList(Map<String, String> requestMap, Page page) {
		return userDao.queryUserList(requestMap, page);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
