<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/admin.js"></script>
<div class="t_header">
	<div class="top_left">
		欢迎，
		<s:property value="#session.user.ACCOUNT" /> <label style="margin-left: 20px;"><a href="/user/logout.action" class="my_button w_50">退出</a></label>
	</div>
</div>