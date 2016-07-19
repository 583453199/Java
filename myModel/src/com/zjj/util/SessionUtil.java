package com.zjj.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.zjj.bean.UserBean;

public class SessionUtil {

	public static void putUserInSession(UserBean bean) {
		Map<String, Object> userInfo = new HashMap<String, Object>();
		if (bean != null) {
			userInfo.put("USER_ID", bean.getUserId() + "");
			userInfo.put("ACCOUNT", bean.getAccount());
			userInfo.put("USER_NAME", bean.getUserName());
			userInfo.put("AGE", bean.getAge() + "");
			userInfo.put("USER_TYPE", bean.getUserType() + "");
		}
		Map<String, Object> session = ActionContext.getContext().getSession();
		session.put("user", userInfo);

		// Subject subject = SecurityUtils.getSubject();
		// Session session = subject.getSession();
		// session.setAttribute("users", userInfo);
	}

	@SuppressWarnings("unchecked")
	public static String getUserSessionInfo(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		key = key.toUpperCase();
		Map<String, Object> session = ActionContext.getContext().getSession();
		Map<String, Object> userMap = (Map<String, Object>) session.get("user");
		if (MapUtils.isEmpty(userMap)) {
			return null;
		}
		return  (String) userMap.get(key);
	}
}
