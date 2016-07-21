package com.zjj.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.zjj.bean.UserBean;
import com.zjj.service.UserService;
import com.zjj.util.CommonUtil;
import com.zjj.util.MyMD5Util;
import com.zjj.util.SessionUtil;

public class AsynAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserService userService;
	private String account;
	private String password;
	private String msg;

	/**
	 * 异步登录
	 * 
	 * @return
	 */
	public String login() {
		if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
			msg = "账号或密码不能为空！";
			return Action.SUCCESS;
		}
		
		String passwordMD5 = MyMD5Util.getStrToMD5(password);
		boolean subjectStatus = CommonUtil.subjectLogin(account, passwordMD5);
		if (subjectStatus == false) {
			msg = "账号或密码错误！";
			return Action.SUCCESS;
		}
		UserBean user = userService.queryUserByInfo(account, passwordMD5);
		if (user == null) {
			msg = "账号或密码错误！";
			return Action.SUCCESS;
		}
		
		// 登录成功，将用户基本信息存入session
		SessionUtil.putUserInSession(user);
		return Action.SUCCESS;
	}

	@JSON(serialize=false)
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
