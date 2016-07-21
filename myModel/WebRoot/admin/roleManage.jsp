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
<title>管理员后台 - 角色管理</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="/css/common.css">
<link rel="stylesheet" type="text/css" href="/css/admin.css">
<link rel="stylesheet" type="text/css" href="/js/pager/pager.css">
<link rel="stylesheet" type="text/css" href="/css/zxxbox.3.0.css">
<style type="text/css">
.resource_add tr {height: 40px;line-height: 40px;}
</style>
</head>
<body id="roleManage">
	<jsp:include page="/admin/adminTop.jsp"></jsp:include>
	<div class="adminContent">
		<jsp:include page="/admin/adminLeft.jsp"/>
		<div class="rightContent">
			<form action="admin/roleManage.action" id="form1" method="post">
				<s:hidden name="page.currentPage" id="currentPage"/>
               	<s:hidden name="page.pageCount" id="pageCount"/>
				<table class="search_table">
					<tbody>
						<tr>
							<td><strong>角色名：</strong><s:textfield name="requestMap.description" placeholder="请输入角色名" cssClass="input02 w_200"></s:textfield></td>
							<td>
								<label style="float: right;">
									<span><a href="javascript:;" id="subSearch" class="my_button w_70 color01">搜索</a></span>
									<span class="ml_10 mr_20"><a href="javascript:;" id="roleAdd" class="my_button w_70">角色新增</a></span>
								</label>
							</td>
						</tr>
					</tbody>
				</table>
				<table class="bordered">
					<tbody>
						<colgroup>
						 	<col width="20%"/>
						 	<col width="20%"/>
						 	<col width="20%"/>
						 	<col width="20%"/>
						 	<col width="20%"/>
						</colgroup>						
						<thead>
							<tr>
								<th>编号</th>
								<th>创建时间</th>
								<th>名称</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<s:if test="list == null || list.size() == 0">
							<tr><td colspan="5" style="text-align: center;">没有数据！</td></tr>
						</s:if>
						<s:else>
							<s:iterator value="list" var="bean">
								<tr>
								 
									<td><s:property value="#bean.ROLE_ID"/></td>
									<td><s:property value="#bean.CREATE_TIME"/></td>
									<td><s:property value="#bean.DESCRIPTION"/></td>
									<td>
										<s:if test="#bean.STATUS == 0">正常</s:if>
										<s:else>删除</s:else>
									</td>
									<td>
										<s:if test="#bean.STATUS == 0 && #bean.BUILTIN == 1">
											<a href="javascript:;" class="x_button w_50 color02">删除</a>
											<a href="<%=basePath%>admin/roleResourceEdit.action?id=<s:property value="#bean.ROLE_ID"/>" class="x_button w_80 color02">角色资源授权</a>
										</s:if>
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
	
	<div id="addBox" style="display: none;">
		<p id="addMsg" class="red mt_10" style="text-align: center;">&nbsp;</p>
		<table style="width: 340px;text-align: center;margin: 10px;" class="resource_add">
			<colgroup>
				<col width="20%"/>
				<col width="80%"/>
			</colgroup>
			<tbody>
				<tr><td>角色名：</td><td><input type="text" id="roleName" class="input02 w_200 fl" maxlength="50"/></td></tr>
				<tr><td colspan="2"><a href="javascript:;" class="my_button w_50 color04" onclick="add()">确定</a></td></tr>
			</tbody>
		</table>
	</div>
	<%@include file="/admin/adminBottom.jsp" %>
	<script type="text/javascript" src="/js/jquery.placeholder.js"></script>
	<script type="text/javascript" src="/js/pager/pager.js"></script>
	<script type="text/javascript" src="/js/jquery.zxxbox.3.0.js"></script>
	<script type="text/javascript">
		$(function() {
			var pageCount = $("#pageCount").val();
			var totalPage = parseInt(pageCount);
			
			var currentPage = $("#currentPage").val();
			var pageNo = parseInt(currentPage);
			
			if (!pageNo) {
				pageNo = 1;
			}
			if(totalPage > 1) {
				pager.generPageHtml({
					pagerid : 'pagerNav',
					pno : pageNo,
					total : totalPage,
					isGoPage : true,
					i18n : 'zh',
					mode : 'click',
					click : function(n) {
						$("#currentPage").val(""+n);
						$("#form1").submit();
						return false;
					}
				});
			}
			
			$("#subSearch").bind("click", function(){
				$("#currentPage").val("1");
				$("#form1").submit(); 
			});
			
			$("#roleAdd").bind("click", function(){
				$("#addBox").zxxbox({title:"角色新增",width:340});
			});
		})
		
		function add() {
			var roleName = $("#roleName").val();
			if (!roleName) {
				$("#addMsg").html("角色名不能为空！");
				return;
			}
			var map = {"description":roleName};
			$.ajax({
				url:"/adminAsyn/addRole.action",
				data: map,
				dataType:"json",
				type: "post",
				success:function(data) {
					var resultCode = data.resultCode;
					if (resultCode == 2) {
						$("#addMsg").html("角色名已存在！");
					} else {
						var msg = resultCode == 0 ? "添加成功！" : "添加失败！";
						$.zxxbox.remind(msg, function() {
							$("#subSearch").click();
						},{btnclose:false,width:350})
					}
				}
			})
		}
	</script>
</body>
</html>
