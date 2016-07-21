<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>用户登录</title>
<link type="text/css" rel="stylesheet" href="css/common.css" />
<link type="text/css" rel="stylesheet" href="css/index.css" />
<link rel="stylesheet" type="text/css" href="/css/zxxbox.3.0.css">
</head>

<body>
	<form action="user/login.action" id="form1" method="post">
	<div style="margin: 300px auto;width: 1000px;">
			<p>账号：<input type="text" id="login_account" name="account" class="input01 mt_10" value="admin" /></p>
			<p>密码：<input type="password" id="login_password" name="password" class="input01 mt_10" value="123456" /></p>
			<a href="javascript:;" class="x_button w_50" onclick="login()">登录</a>
	</div>
	</form>
	<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="/js/jquery.zxxbox.3.0.js"></script>
	<script type="text/javascript">
		function login() {
			var loginAccount = $("#login_account").val();
			var loginPassword = $("#login_password").val();
			if (!loginAccount) {
				$.zxxbox.remind("请输入账号！");
				return;
			} else if (!loginPassword) {
				$.zxxbox.remind("请输入密码！");
				return;
			}
			$.ajax({
				url:"/asyn/login.action",
				data: {"account":loginAccount,"password":loginPassword},
				dataType:"json",
				type: "post",
				success:function(data) {
					var info = data.msg;
					if (info) {
						$.zxxbox.remind(info);
					} else {
						location.href = $("base").attr("href") + "user/index.action";
					}
				}
			})
		}
	</script>
</body>
</html>
