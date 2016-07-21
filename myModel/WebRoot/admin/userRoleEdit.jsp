<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
<title>管理员后台 - 用户角色授权</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="/css/common.css">
<link rel="stylesheet" type="text/css" href="/css/admin.css">
</head>
<body id="userManage">
	<jsp:include page="/admin/adminTop.jsp"></jsp:include>
	<div class="adminContent">
		<jsp:include page="/admin/adminLeft.jsp"/>
		<div class="rightContent">
		<form action="admin/userRoleSub.action" id="form1" method="post">
			<s:hidden name="id"/>
			<div style="margin: 20px;">
				<s:iterator value="list" var="st">
					<label class="mr_10"><input type="checkbox" name="content" value="<s:property value="#st.ROLE_ID"/>" <s:if test="#st.USER_ID != null && #st.USER_ID != ''">checked="checked"</s:if>/>&nbsp;<s:property value="#st.DESCRIPTION"/></label>
				</s:iterator>
				<br/>
				<a href="javascript:;" class="mt_10 my_button w_100" onclick="sub()">确定</a>
			</div>				
		</form>
		</div>
	</div>
	<%@include file="/admin/adminBottom.jsp" %>
	<script type="text/javascript">
		function sub() {
			$("#form1").submit();
		}
	</script>
</body>
</html>
