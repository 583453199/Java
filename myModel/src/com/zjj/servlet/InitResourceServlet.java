package com.zjj.servlet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zjj.bean.ShiroResourceBean;
import com.zjj.service.ShiroService;
import com.zjj.shiro.filter.ResourceFilter;
import com.zjj.shiro.resource.ActionURLResource;
import com.zjj.shiro.resource.NamespaceResource;
import com.zjj.shiro.resource.ResourceManage;
import com.zjj.shiro.util.ShiroHelper;

public class InitResourceServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2585093463495314085L;
	
	private static DefaultFilterChainManager filterChainManager = new DefaultFilterChainManager();

	static {
		// 自定义的 Filter
		filterChainManager.addFilter(ResourceFilter.RESOURCE_FILTER_NAME,
				(Filter) ClassUtils.newInstance(ResourceFilter.class));
	}

	@Override
	public ServletContext getServletContext() {
		return super.getServletContext();
	}

	@Override
	public void init() throws ServletException {
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(super.getServletContext());
		ShiroService shiroService = (ShiroService) wac.getBean("shiroService");
		
		// 获取所有资源信息
		List<ShiroResourceBean> resourceList = shiroService.queryProtectedResourceList();
		if (CollectionUtils.isEmpty(resourceList)) {
			return;
		}
		List<NamespaceResource> newNamespaceResourceList = new LinkedList<NamespaceResource>();
		Map<String, List<ActionURLResource>> newActionUriResourceMap = new HashMap<String, List<ActionURLResource>>();
		for (ShiroResourceBean resource : resourceList) {
			int id = resource.getId();
			int type = resource.getType();
			String url = resource.getUrl();
			if (StringUtils.isEmpty(url)) {
				continue;
			}
			if (ResourceManage.RESOURCE_TYPE_NAMESPACE == type) {
				// struts 命名空间的路径
				newNamespaceResourceList.add(new NamespaceResource(id, url));
			} else {
				int questionMarkPos = url.indexOf("?");
				if (questionMarkPos > -1) { 
					// 带查询参数
					String uri = url.substring(0, questionMarkPos);
					String queryString = url.substring(questionMarkPos + 1);
					List<ActionURLResource> actionURLResourceList = ensureActionURLResourceList(uri,
							newActionUriResourceMap);
					actionURLResourceList.add(new ActionURLResource(id, ShiroHelper.getParamSet(queryString)));
				} else {
					// 不带查询参数
					List<ActionURLResource> actionURLResourceList = ensureActionURLResourceList(url,
							newActionUriResourceMap);
					actionURLResourceList.add(new ActionURLResource(id));
				}
			}
		}
		ResourceManage.nameSpaceResourceList = newNamespaceResourceList;
		ResourceManage.actionUriResourceMap = newActionUriResourceMap;
		
		// 加载角色与资源的访问关系
		ResourceManage.resourceIdRolesMap = shiroService.queryResourceAndRoleMap();
	}
	
	/**
	 * 
	 * @param uri
	 * @param newActionUriResourceMap
	 * @return
	 */
	public static List<ActionURLResource> ensureActionURLResourceList(String uri,
			Map<String, List<ActionURLResource>> newActionUriResourceMap) {
		List<ActionURLResource> actionURLResourceList = newActionUriResourceMap.get(uri);
		if (actionURLResourceList == null) {
			actionURLResourceList = new LinkedList<ActionURLResource>();
			newActionUriResourceMap.put(uri, actionURLResourceList);
		}
		return actionURLResourceList;
	}
}
