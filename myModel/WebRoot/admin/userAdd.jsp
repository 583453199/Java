<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String msg = request.getParameter("msg");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>管理员后台 - 用户新增</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="/css/admin.css">
<link rel="stylesheet" type="text/css" href="/css/common.css">
<link rel="stylesheet" type="text/css" href="/css/zxxbox.3.0.css">
</head>
<body id="userAdd">
	<jsp:include page="/admin/adminTop.jsp"></jsp:include>
	<div class="adminContent">
		<jsp:include page="/admin/adminLeft.jsp"/>
		<div class="rightContent">
			<form action="admin/userAdd.action" id="form1" method="post">
				${msg}
				<table style="width: 300px;margin: 20px 20px;">
					<caption><strong>用户新增</strong></caption>
					<colgroup>
					 	<col width="20%">
                        <col width="80%">
					</colgroup>	
					<tbody>
						<tr><td>账号：</td><td><input type="text" id="account" name="requestMap.account" class="input02 w_200 mt_10"/></td></tr>
						<tr><td>姓名：</td><td><input type="text" value="学生" name="requestMap.userName" class="input02 w_200  mt_10"/></td></tr>
						<tr><td>密码：</td><td><input type="password" value="123456" name="requestMap.password" class="input02 w_200  mt_10"/></td></tr>
						<tr><td>年龄：</td><td><input type="text" value="20" name="requestMap.age" class="input02 w_200  mt_10"/></td></tr>
						<tr><td>类型：</td><td>
							<select name="requestMap.userType" class="select01 mt_10">
								<option value="1">学生</option>
								<option value="2">老师</option>
								<option value="3">管理员</option>
							</select>
						</td></tr>
						  
						<tr><td colspan="2" style="text-align: center;"><a href="javascript:;" class="mt_10 button w_100" onclick="sub()">确定</a></td></tr>
						<tr><td colspan="2" style="text-align: center;color: red;">
							<% if("0".equals(msg)) {%>添加用户成功！<%} else if ("1".equals(msg)) {%>添加用户失败！<%} %>
							</td>
						</tr>
					</tbody>
				</table>
             </form>
		</div>
	</div>
	<script type="text/javascript" src="/js/jquery.zxxbox.3.0.js"></script>
	<script type="text/javascript">
		function sub() {
			if (!$("#account").val()) {
				$.zxxbox.remind("请输入账号！");
				return;
			}
			$("#form1").submit();
		}
	</script>
</body>
</html>
