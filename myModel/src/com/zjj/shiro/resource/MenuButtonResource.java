package com.zjj.shiro.resource;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.subject.Subject;

public class MenuButtonResource implements Permission {
	private Set<String> availableResources; // 存储用户能访问的菜单、按钮资源
	private String accessResource; // 期望访问的资源

	public MenuButtonResource(String accessResource) {
		if (accessResource == null) {
			accessResource = "";
		}
		this.accessResource = accessResource;
	}

	public MenuButtonResource(Set<String> availableResources) {
		if (availableResources == null) {
			availableResources = new HashSet<String>();
		}
		this.availableResources = availableResources;
	}

	public boolean implies(Permission p) {
		Subject subject = SecurityUtils.getSubject();
		String userId = (String) subject.getPrincipal();
		// 超级管理员帐号允许访问
		if (ResourceManage.SUPER_ADMIN_ID.equals(userId) && subject.isAuthenticated()) {
			return true;
		}
//		if (p instanceof MenuButtonResource == false) {
//			return false;
//		}
		MenuButtonResource verifiedPermission = (MenuButtonResource) p;
		String accessResource = verifiedPermission.accessResource;

		return this.availableResources.contains(accessResource);
	}
}
