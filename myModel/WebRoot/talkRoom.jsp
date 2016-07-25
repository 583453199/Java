<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Language" content="zh-cn" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Comet4</title>
	<link rel="stylesheet" type="text/css" href="/css/comet4j_style.css">
<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/comet4j.js"></script>
<script type="text/javascript" src="/js/talk.js"></script>
</head>
<body onload="init()">


<input type="hidden" name="userName" value="student"/>
<input type="hidden" name="userId" value="<s:property value="#session.user.USER_ID"/>"/>

<div id="statebar">
连接状态：<span id="workStyle"></span>；
连接数量：<span id="connectorCount"></span>；
</div>

<div id="logbox">
</div>

<div id="toolbar" >
请输入：<input maxlength="200" id="inputbox" class="inputbox" onkeypress="return onSendBoxEnter(event);" type="text" ></input>
<input type="button" class="button" onclick="send(inputbox.value);" value="回车发送"></input>
<input type="button" class="button" onclick="rename();" value="改名"></input>
</div>

<div id="login">
	请输入昵称：<input type="text" class="inputbox" maxlength="50" id="loginName" onkeypress="return loginEnter(event);"></input>
	<input type="button" class="button" onclick="login();" value="确定"></input>
</div>

</body>
</html>
