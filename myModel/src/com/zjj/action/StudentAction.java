package com.zjj.action;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class StudentAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7032861181936643243L;

	@SuppressWarnings("unchecked")
	public String studentIndex() {
		Map<String,Object> session = ActionContext.getContext().getSession();
		Map<String,Object> userMap = (Map<String,Object>) session.get("user");
		if (MapUtils.isNotEmpty(userMap)) {
			System.out.println("当前登录用户的账号为：" + userMap.get("ACCOUNT"));
		}
		
		Subject subject = SecurityUtils.getSubject();  
		System.out.println(subject.hasRole("2") + "---" + subject.isPermitted("z4"));
		return "index";
	}
}