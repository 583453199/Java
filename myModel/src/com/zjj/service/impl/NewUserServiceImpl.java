package com.zjj.service.impl;

import java.util.Map;

import com.zjj.bean.UserBean;
import com.zjj.dao.NewUserDao;
import com.zjj.service.NewUserService;
import com.zjj.util.common.Page;

public class NewUserServiceImpl implements NewUserService {

	private NewUserDao newUserDao;

	@Override
	public boolean login(String account, String password) {
		return newUserDao.login(account, password);
	}
	
	@Override
	public UserBean queryUserByInfo(String account, String password) {
		return newUserDao.queryUserByInfo(account, password);
	}
	
	@Override
	public String addUser(UserBean userBean) {
		return newUserDao.addUser(userBean);
	}
	
	@Override
	public Map<String, Object> queryUserList(Map<String, String> requestMap, Page page) {
		return newUserDao.queryUserList(requestMap, page);
	}

	public NewUserDao getNewUserDao() {
		return newUserDao;
	}

	public void setNewUserDao(NewUserDao newUserDao) {
		this.newUserDao = newUserDao;
	}

}
