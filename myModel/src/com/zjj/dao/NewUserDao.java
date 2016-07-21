package com.zjj.dao;

import java.util.Map;

import com.zjj.bean.UserBean;
import com.zjj.util.common.Page;

public interface NewUserDao {
	
	/**
	 * 登录
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public boolean login(String account, String password);

	/**
	 * 根据账号、密码获取用户信息
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public UserBean queryUserByInfo(String account, String password);
	
	/**
	 * 添加用户
	 * 
	 * @param userBean
	 * @return
	 */
	public String addUser(UserBean userBean);
	
	/**
	 * 查询用户集合
	 * 
	 * @param requestMap
	 * @param page
	 * @return
	 */
	public Map<String, Object> queryUserList(Map<String, String> requestMap, Page page);
}
