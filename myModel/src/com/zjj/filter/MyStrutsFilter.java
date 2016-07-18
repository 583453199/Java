package com.zjj.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import com.zjj.shiro.resource.ResourceManage;

public class MyStrutsFilter extends StrutsPrepareAndExecuteFilter {
	private static final String ASYN_EXPORT_DATA = "asynExportData";
	public static final String RESOURCE_FILTER_NAME = "resource";
	private static final String LOGIN_URL = "/";
	private static final String UNAUTHORIZED_URL = "/unauthorized.html";

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		String queryString = httpRequest.getQueryString();
		boolean allowAccess = ResourceManage.allowAccess(requestURI, queryString, httpRequest);
		if (allowAccess == false) {
			Subject subject = SecurityUtils.getSubject();
			if (subject.isAuthenticated()) {
				// 已经登录，去没有权限页面
				toUnauthorizedPage(httpRequest, response);
			} else {
				// 去登录页面
				toLogin(httpRequest, response);
			}
			return;
		}
		super.doFilter(request, response, chain);
	}

	private static boolean isAjax(HttpServletRequest httpRequest) {
		return "XMLHttpRequest".equalsIgnoreCase(httpRequest.getHeader("X-Requested-With"))
				|| "true".equals(httpRequest.getParameter(ASYN_EXPORT_DATA));
	}

	private static void toUnauthorizedPage(HttpServletRequest httpRequest, ServletResponse response) throws IOException {
		if (isAjax(httpRequest)) { // ajax请求
			PrintWriter printWriter = response.getWriter();
			printWriter.print("{noPermission:true}");
			printWriter.flush();
			printWriter.close();
			return;
		}
		WebUtils.issueRedirect(httpRequest, response, UNAUTHORIZED_URL);
	}

	public static void toLogin(HttpServletRequest httpRequest, ServletResponse response) throws IOException {
		if (isAjax(httpRequest)) { // ajax请求
			PrintWriter printWriter = response.getWriter();
			printWriter.print("{sessionTimeout:true}");
			printWriter.flush();
			printWriter.close();
			return;
		}
		WebUtils.issueRedirect(httpRequest, response, LOGIN_URL);
	}
}
