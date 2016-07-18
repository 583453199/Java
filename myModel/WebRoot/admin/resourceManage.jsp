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
<title>管理员后台 - 资源管理</title>
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
<body id="resourceManage">
	<jsp:include page="/admin/adminTop.jsp"></jsp:include>
	<div class="adminContent">
		<jsp:include page="/admin/adminLeft.jsp"/>
		<div class="rightContent">
			<form action="admin/resourceManage.action" id="form1" method="post">
				<s:hidden name="page.currentPage" id="currentPage"/>
               	<s:hidden name="page.pageCount" id="pageCount"/>
				<table class="search_table">
					<tbody>
						<tr>
							<td><strong>资源描述：</strong><s:textfield name="requestMap.description" placeholder="请输入资源描述" cssClass="input02 w_200"></s:textfield></td>
							<td><strong>资源URL：</strong><s:textfield name="requestMap.url" placeholder="请输入资源URL" cssClass="input02 w_200"></s:textfield></td>
							<td>
								<label style="float: right;">
									<span><a href="javascript:;" id="subSearch" class="my_button w_70 color01">搜索</a></span>
									<span class="ml_10 mr_20"><a href="javascript:;" id="resourceAdd" class="my_button w_80">资源新增</a></span>
								</label>
							</td>
						</tr>
					</tbody>
				</table>
				<table class="bordered">
					<tbody>
						<colgroup>
						 	<col width="12%"/>
	                        <col width="12%"/>
	                        <col width="15%"/>
	                        <col width="15%"/>
	                        <col width="20%"/>
	                        <col width="12%"/>
	                        <col width="15%"/>
						</colgroup>						
						<thead>
							<tr>
								<th>编号</th>
								<th>类型</th>
								<th>值</th>
								<th>描述</th>
								<th>url</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<s:if test="list == null || list.size() == 0">
							<tr><td colspan="7" style="text-align: center;">没有数据！</td></tr>
						</s:if>
						<s:else>
							<s:iterator value="list" var="bean">
								<tr>
									<td><s:property value="#bean.ID"/></td>
									<td>
										<s:if test="#bean.TYPE == 0">命名空间</s:if>
										<s:else>菜单、按钮</s:else>
									</td>
									<td><s:property value="#bean.VALUE"/></td>
									<td><s:property value="#bean.DESCRIPTION"/></td>
									<td><s:property value="#bean.URL"/></td>
									<td>
										<s:if test="#bean.STATUS == 0">正常</s:if>
										<s:else>删除</s:else>
									</td>
									<td>
										<s:if test="#bean.STATUS == 0">
											<a href="javascript:;" class="my_button w_50 color02">删除</a>
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
		<table style="width: 340px;text-align: center;" class="resource_add">
			<colgroup>
				<col width="20%"/>
				<col width="80%"/>
			</colgroup>
			<tbody>
				<tr><td>类型：</td>
					<td>
						<select class="select01 fl" id="type">
							<option value="1">菜单</option>
							<option value="0">命名空间</option>
						</select>
					</td>
				</tr>
				<tr><td>值：</td><td><input type="text" id="value" class="input02 w_200 fl"/></td></tr>
				<tr><td>描述：</td><td><input type="text" id="description" class="input02 w_200 fl"/></td></tr>
				<tr><td>url：</td><td><input type="text" id="url" class="input02 w_200 fl"/></td></tr>
				<tr><td colspan="2"><a href="javascript:;" class="my_button w_50 color04" onclick="addResource()">确定</a></td></tr>
				<tr><td colspan="2" id="addMsg">&nbsp;</td></tr>
			</tbody>
		</table>
	</div>
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
			
			$("#resourceAdd").bind("click", function(){
				$("#addBox").zxxbox({title:"资源新增",width:340});
			});
		})
		
		function addResource() {
			var value = $("#value").val();
			var description = $("#description").val();
			var url = $("#url").val();
			if (!value) {
				$("#addMsg").html("请完善资源值！");
				return;
			} else if (!description) {
				$("#description").html("请完善资源描述！");
				return;
			} 
			var map = {"value":value,"description":description,"url":url};
			$.ajax({
				url:"/asyn/addResourceSubmit.action",
				data: map,
				dataType:"json",
				type: "post",
				success:function(data) {
					var msg = data.statusCode ? "添加成功！" : "添加失败！";
					$.zxxbox.remind(msg, function() {
						$("#subSearch").click();
					},{btnclose:false})
				}
			})
		}
	</script>
</body>
</html>
