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
<title>管理员后台 - 角色 - 资源 - 授权</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="/css/common.css">
<link rel="stylesheet" type="text/css" href="/css/admin.css">
<link rel="stylesheet" type="text/css" href="/css/zxxbox.3.0.css">
<link rel="stylesheet" type="text/css" href="/js/ztree/zTreeStyleBack.css">

<style type="text/css">
.resource_add tr {height: 40px;line-height: 40px;}
</style>
</head>
<body id="roleManage">
	<jsp:include page="/admin/adminTop.jsp"></jsp:include>
	<div class="adminContent">
		<jsp:include page="/admin/adminLeft.jsp"/>
		<div class="rightContent">
			<form action="admin/roleResourceEdit.action" id="form1" method="post">
				<input type="hidden" name="id" id="roleId" value="${id}"/>
				<ul id="tree" class="ztree"></ul>
				<a href="javascript:;" class="my_button color03 subAdd" style="width: 50px;margin: 20px;" onclick="operateSub()">提交</a>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="/js/jquery.placeholder.js"></script>
	<script type="text/javascript" src="/js/jquery.zxxbox.3.0.js"></script>
	<script type="text/javascript" src="/js/ztree/jquery.ztree.core.custom.js"></script>
	<script type="text/javascript" src="/js/ztree/jquery.ztree.excheck.min.js"></script>
	
	<script type="text/javascript">
	var setting = {
			view: {
				selectedMulti: false
			},
			check: {
				enable: true
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeCheck: zTreeBeforeCheck,
				onCheck: zTreeOnCheck,
				onClick: zTreeOnClick
			}
		};	
	
		var data = '<s:property  value="content" escapeHtml="false" />';
		var zNodes = $.parseJSON(data);
		function zTreeOnClick(e, treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			treeObj.expandNode(treeNode, !treeNode.open, true, true);
		}
		
		function zTreeBeforeCheck(treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			if (treeNode.checked) {
				var node = treeNode.getParentNode();
				node.checked = true;
			}
		}
		
		//节点被选
		function zTreeOnCheck(e, treeId, treeNode) {
			// 如果有叶子节点被勾选，其同级的'查询'节点也应该被勾选（权限设计方案的要求）
			if (treeNode.isParent == false) {
				var treeObj = $.fn.zTree.getZTreeObj("tree");
				var srcNode = treeNode;
				if (srcNode.checked)　{　// 叶子节点被勾选
					while (treeNode = treeNode.getPreNode()) { // 前一个节点
						doCheckQueryNode(treeObj,treeNode);
					}
					treeNode = srcNode;
					while (treeNode = treeNode.getNextNode()) { // 后一个节点
						doCheckQueryNode(treeObj,treeNode);
					}
				} else if (srcNode.name == '查询') { // '查询'叶子节点去掉勾选
					while (treeNode = treeNode.getPreNode()) { // 前一个节点
						if (treeNode.checked) {
							doCheckQueryNode(treeObj,srcNode);
							return;
						}
					}
					treeNode = srcNode;
					while (treeNode = treeNode.getNextNode()) { // 后一个节点
						if (treeNode.checked) {
							doCheckQueryNode(treeObj,srcNode);
							return;
						}
					}
				}
			}
		}
		
		$(document).ready(function(){
			$.fn.zTree.init($("#tree"), setting, zNodes);
		});
		
		function zxxBoxRemind(message, event) {
			$.zxxbox.remind(message, event, {
				title : "提示：",
				width : 400,
				bgclose : true,
				btnclose : false,
				drag : true
			});
		}
		
		function doCheckQueryNode(treeObj,treeNode) {
			if (treeNode.checked == false && treeNode.name == '查询') {
				treeNode.checked = true;
				treeObj.updateNode(treeNode);
			}
		}
		
		function operateSub() {
			var roleId = $("#roleId").val();
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.getCheckedNodes(true);
			var selectedIds = "";
			for(var i=0;i< nodes.length;i++) {
				selectedIds += nodes[i].db_id;
				if (i < nodes.length - 1) {
					selectedIds += ",";	
				}
			}
			$.ajax({
				url : "admin/roleResourceSub.action",
				data: {"role_id":roleId,"selected_ids":selectedIds},
				dataType : "json",
				type : "post",
				async : false,
				success : function(data) {
					var resultCode = data.RESULT_CODE;
					if (resultCode == 0) {
						zxxBoxRemind("操作成功！",function(){
							$("#form1").submit();
						});
					} else {
						zxxBoxRemind("操作失败！",null);
					}
				}
			})
		}
		
		function hideZxxBox() {
			$("#noteName").val("");
			$.zxxbox.hide();
		}
		
		function addNoteSub() {
			var noteName = $.trim($("#noteName").val());
			if (!noteName) {
				$("#msg").html("模块名不能为空！");
			} else {
				var treeObj = $.fn.zTree.getZTreeObj("tree");
				var nodes = treeObj.getSelectedNodes();
				var treeNode = nodes[0]; 
				var newNodes = treeObj.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:noteName});
				if (newNodes) {
					hideZxxBox();
				}
			}
		}
	</script>
</body>
</html>
