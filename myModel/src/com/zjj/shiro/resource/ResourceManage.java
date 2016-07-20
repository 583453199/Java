package com.zjj.shiro.resource;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.zjj.shiro.util.ShiroHelper;

public class ResourceManage {
	
	// struts NameSpace 资源类型
	public static final int RESOURCE_TYPE_NAMESPACE = 0;
	
	public static final String SUPER_ADMIN_ID = "80";
	
	// 命名空间部分
	public static List<NamespaceResource> nameSpaceResourceList = new LinkedList<NamespaceResource>();

	// struts action url 受控资源 (分2部分，uri:paramMap)
	public static Map<String, List<ActionURLResource>> actionUriResourceMap = new HashMap<String, List<ActionURLResource>>();
	
	// 资源、角色映射关系
	public static Map<String, Set<String>> resourceIdRolesMap = new HashMap<String, Set<String>>();
	
	public static boolean allowAccess(String requestURI, String queryString, HttpServletRequest httpRequest) {
		Subject subject = SecurityUtils.getSubject();

		Set<String> paramSet = ShiroHelper.getParamSet(queryString);
		Set<String> protectedResourceIds = matchedProtectedResourceIds(requestURI, paramSet);
		if (CollectionUtils.isEmpty(protectedResourceIds)) {
			return true;
		}
		if (MapUtils.isEmpty(resourceIdRolesMap)) {
			return true;
		}
		// 没有登录
		if (subject.isAuthenticated() == false) { 
			return false;
		}
		
		// 要求能访问匹配到的所有受控资源
		for (String resourceId : protectedResourceIds) {
			Set<String> roles = resourceIdRolesMap.get(resourceId); // 允许访问该资源的角色
			// 没有一个角色可以访问该资源
			if (CollectionUtils.isEmpty(roles)) {
				return false;
			}
			
			// 如果属于其中一个角色，即可访问该资源
			boolean hasRole = false;
			for (String role : roles) {
				if (subject.hasRole(role)) {
					hasRole = true;
					break;
				}
			}
			if (hasRole == false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param requestURI
	 * @param requestParamMap
	 * @return 匹配到的受控资源编号列表。（只有用户同时被授权访问这些资源，系统才允许这次request）
	 */
	private static Set<String> matchedProtectedResourceIds(String requestURI, Set<String> requestParamSet) {
		Set<String> idSet = new LinkedHashSet<String>();
		for (NamespaceResource namespaceResource : ResourceManage.nameSpaceResourceList) {
			if (namespaceResource.match(requestURI)) {
				idSet.add(String.valueOf(namespaceResource.getId()));
			}
		}
		List<ActionURLResource> actionUriResourceList = ResourceManage.actionUriResourceMap.get(requestURI);
		if (CollectionUtils.isEmpty(actionUriResourceList)) {
			return idSet;
		}
		for (ActionURLResource actionUrlResource : actionUriResourceList) {
			if (actionUrlResource.match(requestParamSet)) {
				idSet.add(String.valueOf(actionUrlResource.getId()));
			}
		}
		return idSet;
	}
}
