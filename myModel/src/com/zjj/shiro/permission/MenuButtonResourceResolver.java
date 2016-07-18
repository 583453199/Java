package com.zjj.shiro.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;

import com.zjj.shiro.resource.MenuButtonResource;

public class MenuButtonResourceResolver implements PermissionResolver {

	@Override
	public Permission resolvePermission(String accessResource) {
		return new MenuButtonResource(accessResource);
	}

}
