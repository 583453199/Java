package com.zjj.util;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.zjj.bean.UserBean;

public class SessionUtil {

	public static void putUserInSession(UserBean bean) {
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("USER_ID", bean.getUserId());
		userInfo.put("ACCOUNT", bean.getAccount());
		userInfo.put("USER_NAME", bean.getUserName());
		userInfo.put("AGE", bean.getAge());
		userInfo.put("USER_TYPE", bean.getUserType());

		Map<String, Object> session = ActionContext.getContext().getSession();
		session.put("user", userInfo);
		
//		Subject subject = SecurityUtils.getSubject();  
//		Session session = subject.getSession();  
//		session.setAttribute("users", userInfo);
	}
}
