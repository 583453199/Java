package com.zjj.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;

import com.opensymphony.xwork2.ActionSupport;
import com.zjj.bean.UserBean;
import com.zjj.service.UserService;
import com.zjj.util.CommonUtil;
import com.zjj.util.MyMD5Util;
import com.zjj.util.SessionUtil;

public class UserAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7032861181936643243L;

	private UserService userService;
	private Map<String, String> requestMap;

	private String account;
	private String password;
	private String msg;

	/**
	 * 用户登录
	 * 
	 * @return
	 */
	public String login() {
		if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
			msg = "账号或密码不能为空！";
			return "login";
		}
		
		String passwordMD5 = MyMD5Util.getStrToMD5(password);

		boolean subjectStatus = CommonUtil.subjectLogin(account, passwordMD5);
		if (subjectStatus == false) {
			msg = "账号或密码错误！";
			return "login";
		}
		UserBean user = userService.queryUserByInfo(account, passwordMD5);
		if (user == null) {
			msg = "账号或密码错误！";
			return "login";
		}
		
		// 登录成功，将用户基本信息存入session
		SessionUtil.putUserInSession(user);
		int userType = user.getUserType();
		if (userType == 1) {
			return "toStudentIndex";
		} else if (userType == 2) {
			return "toTeacherIndex";
		} else if (userType == 3) {
			return "toAdminIndex";
		}
		return "toIndex";
	}
	
	/**
	 * 用户注册
	 * 
	 * @return
	 */
	public String register() {
		String account =  (String) requestMap.get("account");
		String userName = (String) requestMap.get("userName");
		String password = (String) requestMap.get("password");
		String age = (String) requestMap.get("age");
		
		UserBean bean = new UserBean();
		bean.setAccount(account);
		bean.setUserName(userName);
		bean.setPassword(MyMD5Util.getStrToMD5(password));
		bean.setAge(Integer.parseInt(age));
		bean.setUserType(1);
		userService.addUser(bean);
		return "toIndex";
	}
	
	/**
	 * 找回密码
	 * 
	 * @return
	 */
	public String recoverPassword() {
		
		return "recoverPassword";
	}

	public String index() {
		
		return "index";
	}

	@JSON(serialize = false)
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

	public Map<String, String> getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(Map<String, String> requestMap) {
		this.requestMap = requestMap;
	}
	
}