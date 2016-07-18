package com.zjj.shiro.realm;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.zjj.bean.UserBean;
import com.zjj.service.ShiroService;
import com.zjj.shiro.UserAuthenticationInfo;
import com.zjj.shiro.resource.MenuButtonResource;

public class UserRealm extends AuthorizingRealm {

	private ShiroService shiroService;

	@Override
	public String getName() {
		return "userRealm";
	}

	/**
	 * 认证回调函数,登录时调用.
	 * 
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String account = (String) token.getPrincipal();
		String password = new String((char[]) token.getCredentials());

		String userId = shiroService.shiroLogin(account, password);

		if (StringUtils.isBlank(userId)) {
			System.out.println("shiro验证 - 用户名或密码错误！");
			throw new AuthenticationException();
		}
		// 身份认证验证成功，返回一个AuthenticationInfo实现；
		UserAuthenticationInfo authInfo = new UserAuthenticationInfo(userId, password, getName());
		authInfo.setAccount(account);
		return authInfo;
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 * 
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		String userId = (String) principalCollection.getPrimaryPrincipal();
		AuthorizationInfo authorizationInfo = setAuthorizationInfo(userId);
		return authorizationInfo;
	}

	private AuthorizationInfo setAuthorizationInfo(String userId) {
		Set<String> roles = shiroService.queryUserRoles(userId);
		if (CollectionUtils.isEmpty(roles)) {
			roles = new HashSet<String>();
		}

		// 1、学生  2、老师  3、管理员
		// 系统为userType建立了默认的角色,然后这些默认的角色允许访问相关的struts namespace
		int userIdInt = Integer.parseInt(userId);
		UserBean userBean = shiroService.queryUserById(userIdInt);
		roles.add(String.valueOf(userBean.getUserType()));

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 设置角色，应对 anyRoles filter 鉴权
		authorizationInfo.setRoles(roles);

		Set<String> availableResources = shiroService.queryMenuButtonResources(roles);
		// 设置菜单权限、按钮权限
		authorizationInfo.addObjectPermission(new MenuButtonResource(availableResources));
		return authorizationInfo;
	}

	public ShiroService getShiroService() {
		return shiroService;
	}

	public void setShiroService(ShiroService shiroService) {
		this.shiroService = shiroService;
	}

}
