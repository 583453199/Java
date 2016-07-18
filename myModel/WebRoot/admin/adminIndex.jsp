<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="zjj" uri="http://10.204.42.36/s2shModel/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>管理员首页</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="/css/common.css">
<link rel="stylesheet" type="text/css" href="/css/admin.css">
</head>
<body>
	<jsp:include page="/admin/adminTop.jsp"></jsp:include>
	
	<div class="adminContent">
		<jsp:include page="/admin/adminLeft.jsp"/>
		<div>个人首页</div>
	</div>
	<%--<ul>
		<zjj:hasResource value="a1">
			<li>增加</li>
		</zjj:hasResource>
		<zjj:hasResource value="a2">
			<li>删除</li>
		</zjj:hasResource>
		<zjj:hasResource value="a3">
			<li>查看</li>
		</zjj:hasResource>
		<zjj:hasResource value="a4">
			<li>修改</li>
		</zjj:hasResource>
		<li>测试=Hello, <shiro:principal />, how are you today?</li>

		<shiro:hasPermission name="a1">
			<li>增加1</li>
		</shiro:hasPermission>
		<shiro:hasPermission name="a2">
			<li>增加2</li>
		</shiro:hasPermission>
		<shiro:hasPermission name="a3">
			<li>增加3</li>
		</shiro:hasPermission>
		<shiro:hasPermission name="a4">
			<li>增加4</li>
		</shiro:hasPermission>
	</ul>
--%></body>
</html>
