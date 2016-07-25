package com.zjj.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCache;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import com.zjj.shiro.CleanSubjectTask;
import com.zjj.shiro.LoginSubject;

public class CommonUtil {

	private static Log log = LogFactory.getLog(CommonUtil.class);
	
	public static final String RET_STATUS = "RET_STATUS";
	
	public static boolean subjectLogin(String account, String password) {
		boolean status = false;
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(account, password);
		try {
			subject.login(token);
			status = true;
		} catch (UnknownAccountException e) {
			log.error("用户名/密码错误", e);
		} catch (IncorrectCredentialsException e) {
			log.error("用户名/密码错误", e);
		} catch (AuthenticationException e) {
			// 其他错误，比如锁定，如果想单独处理请单独catch处理
			log.error("其他错误：" + e.getMessage());
		}
		if (status) {
			Subject subject1 = SecurityUtils.getSubject();
			String userId = (String) subject1.getPrincipal();
			System.out.println(subject1.isPermitted("3"));
			CleanSubjectTask.put(userId, account, password, subject);
		}
		return status;
	}
	
	public static void cleanShiroCache(String userId) {
		if (userId == null) {
			return;
		}
		List<String> userIdList = new LinkedList<String>();
		userIdList.add(userId);
		cleanShiroCache(userIdList);
	}

	private static void cleanShiroCache(List<String> userIdList) {
		if (userIdList == null) {
			return;
		}
		DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
		CacheManager cacheManager = securityManager.getCacheManager();
		EhCache<Object, Object> authenticationCache = (EhCache<Object, Object>) cacheManager
				.getCache("authenticationCache");
		EhCache<Object, Object> authorizationCache = (EhCache<Object, Object>) cacheManager
				.getCache("authorizationCache");
		for (String userId : userIdList) {
			List<LoginSubject> loginSubjectList = CleanSubjectTask.get(userId);
			if (loginSubjectList == null) {
				continue;
			}
			for (LoginSubject loginSubject : loginSubjectList) {
				String loginName = loginSubject.getLoginName();
				try {
					authenticationCache.remove(loginName);
				} catch (Exception e) {
				}
				Subject subject = loginSubject.getSubject();
				if (subject != null) {
					try {
						authorizationCache.remove(subject.getPrincipals());
					} catch (Exception e) {
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isInvalid(Map result) {
		if (result == null) {
			return true;
		}
			String status = (String) result.get(RET_STATUS);
			return !"S".equalsIgnoreCase(status);
	}
	
	public static String[] getCharArray() {
		return new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
				"s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	}
}
