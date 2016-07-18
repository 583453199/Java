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
<title>管理员后台 - 用户管理</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="/css/common.css">
<link rel="stylesheet" type="text/css" href="/css/admin.css">
<link rel="stylesheet" type="text/css" href="/js/pager/pager.css">
</head>
<body id="userManage">
	<jsp:include page="/admin/adminTop.jsp"></jsp:include>
	<div class="adminContent">
		<jsp:include page="/admin/adminLeft.jsp"/>
		<div class="rightContent">
			<form action="admin/userManage.action" id="form1" method="post">
				<s:hidden name="page.currentPage" id="currentPage"/>
               	<s:hidden name="page.pageCount" id="pageCount"/>
				<table class="search_table">
					<tbody>
						<tr>
							<td><strong>用户信息：</strong><s:textfield name="requestMap.account" placeholder="请输入用户ID或账号信息" cssClass="input02 w_200"></s:textfield></td>
							<td><strong>用户类型 ：</strong>
								<select name="requestMap.userType" class="select01 mt_10">
									<option value="" >全部</option>
									<option value="1" <s:if test="requestMap.userType == 1">selected="selected"</s:if>>学生</option>
									<option value="2" <s:if test="requestMap.userType == 2">selected="selected"</s:if>>老师</option>
									<option value="3" <s:if test="requestMap.userType == 3">selected="selected"</s:if>>管理员</option>
								</select>
							</td>
							<td>
								<label><a href="javascript:;" id="subSearch" class="my_button w_70 color01">搜索</a></label>
								<label class="ml_10"><a href="<%=basePath%>admin/userAdd.jsp" class="my_button w_70">用户新增</a></label>
							</td>
						</tr>
					</tbody>
				</table>
				<table class="bordered">
					<tbody>
						<colgroup>
						 	<col width="15%">
	                        <col width="15%">
	                        <col width="15%">
	                        <col width="15%">
	                        <col width="15%">
	                        <col width="15%">
						</colgroup>						
						<thead>
							<tr>
								<th>用户ID</th>
								<th>账号</th>
								<th>姓名</th>
								<th>年龄</th>
								<th>用户类型</th>
								<th>操作</th>
							</tr>
						</thead>
						<s:if test="userList == null || userList.size() == 0">
							<tr><td colspan="6" style="text-align: center;">没有数据！</td></tr>
						</s:if>
						<s:else>
							<s:iterator value="userList" var="user">
								<tr>
									<td><s:property value="#user.userId"/></td>
									<td><s:property value="#user.account"/></td>
									<td><s:property value="#user.userName"/></td>
									<td><s:property value="#user.age"/></td>
									<td><s:if test="#user.userType == 1">学生</s:if>
										<s:elseif test="#user.userType == 2">老师</s:elseif>
										<s:elseif test="#user.userType == 3">管理员</s:elseif>
									</td>
									<td>
										<a href="javascript:;" class="my_button w_50 color02">删除</a>
									</td>
								</tr>
							</s:iterator>
						</s:else>
					</tbody>
				</table>
				<!-- 10条 翻页 begin -->
				<s:if test="page.pageCount > 1">
					<div id="pagerNav" class="pagination-inner"></div>
				</s:if>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="/js/jquery.placeholder.js"></script>
	<script type="text/javascript" src="/js/pager/pager.js"></script>
	
	<script type="text/javascript">
		$(function() {
			var pageCount = $("#pageCount").val();
			var totalPage = parseInt(pageCount);
			
			var currentPage = $("#currentPage").val();
			var pageNo = parseInt(currentPage);
			
			if(!pageNo){
				pageNo = 1;
			}
			if(totalPage > 1){
				pager.generPageHtml({
					pagerid : 'pagerNav',
					pno : pageNo,
					total : totalPage,
					isGoPage : true,
					i18n : 'zh',
					mode : 'click',
					click : function(n){
						$("#currentPage").val(""+n);
						$("#form1").submit();
						return false;
					}
				});
			}
			
			$("#subSearch").bind("click",function() {
				$("#currentPage").val("1");
				$("#form1").submit(); 
			});
		})
	</script>
</body>
</html>
