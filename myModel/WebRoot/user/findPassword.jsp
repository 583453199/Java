<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE>
<html>
<head>
<base href="<%=basePath%>">
<title>登录</title>
<link type="text/css" rel="stylesheet" href="css/login.css"/>
</head>
<body style="background-color: silver;">
          游客页面访问
    <script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
</body>
</html>