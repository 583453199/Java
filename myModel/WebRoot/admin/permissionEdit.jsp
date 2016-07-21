<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit" />
<title>管理后台 - 模块管理</title>
<%=com.zteict.talk915.util.FileVersion.importCSSFromCSSDir("common.css", "admin.css", "zxxbox.3.0.css")%>
<%=com.zteict.talk915.util.FileVersion.importCSS("js/ztree/zTreeStyleBack.css")%>
<style type="text/css">
	.rightContent {
		margin-left:30px;
		font-size: 14px!important;
	}
	.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
</style>

</head>
<body class="warp_bg admin">
	<jsp:include page="adminPageTop.jsp" />
	<div class="cc adminBody">
		<!-- 侧边栏菜单 begin -->
		<div class="adminSidebar" id="adminSidebar">
			<jsp:include page="/admin/adminNav.jsp" flush="true">
				<jsp:param name="menuid" value="3" />
			</jsp:include>
		</div>
		<!-- 侧边栏菜单 end -->
		<div class="adminContent" id="adminContent">
			<div class="rightContent">
				<form action="/admin/admineeQuestionAddStepOne.action" id="form1" method="post">
					<%--<label>新增根节点 <input type="text" id="description" class="input01" maxlength="50" style="width: 150px;"/><a href="javascript:;" class="button02 color02 subAdd" style="width: 50px;margin-left: 10px;" onclick="addParentNode()">确定</a></label>--%>
					<ul id="tree" class="ztree"></ul>
					<a href="javascript:;" class="button02 color03 subAdd" style="width: 50px;margin-left: 50px;" onclick="operateSub()">提交</a>
				</form>
			</div>
		</div>
	</div>
	
	<div id="addBox" style="display:none;">
	  <div style="margin: 20px;">
	   	<p id="city_msgTips" class="mb_10"  style="color:#FA9652;"></p>
	       <div class="tc mb_10" style="float: left;"  id="protection">
	       	  <p id="msg" class="red"></p>
	       	  <label>模块名：<input type="text" id="noteName" class="input01" maxlength="50"/></label>
	       </div>
	       <div class="tc" style="clear :left">
	       	  <a href="javascript:void(0)" class="btn_small w_60" onclick="addNoteSub()"><em>确定</em></a>&nbsp;&nbsp;
	       	  <a id="cancelBtn" class="btn_small w_60" href="javascript:hideZxxBox();"><em>取消</em> </a>
	       </div>
	   </div>
	</div>
	
	<!-- 页面底部 -->
	<%@include file="/admin/adminBottom.jsp" %>

	<!-- 页面底部 end -->
	<%=com.zteict.talk915.util.FileVersion.importJSFromJSDir(
			"jquery-1.8.3.min.js","common.js","admin.js","jquery.zxxbox.3.0.js",
			"ztree/jquery.ztree.core.custom.js","ztree/jquery.ztree.excheck.min.js","ztree/jquery.ztree.exedit.min.js")%>

	<script type="text/javascript">
		var setting = {
			view: {
				addHoverDom: addHoverDom,
				removeHoverDom: removeHoverDom,
				selectedMulti: false
			},
			edit: {
				drag: {
					autoExpandTrigger: true,
					prev: dropPrev,
					inner: dropInner,
					next: dropNext
				},
				enable: true,
				editNameSelectAll: true,
				showRemoveBtn: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: zTreeOnClick,
				beforeDrag: beforeDrag,
				beforeRename: beforeRename,
				beforeEditName: beforeEditName
			}
		};	
		var zNodes;
		$.ajax({
			url : "asyn/checkloadEEModule.action",
			dataType : "json",
			type : "post",
			async : false,
			success : function(data) {
			    var content = $.parseJSON(data.content);
				zNodes =content;
			}
		})
		
		function beforeDrag(treeId, treeNodes) {
			return false;
		}
		
		function zTreeOnClick(e, treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			treeObj.expandNode(treeNode, !treeNode.open, true, true);
		}
		
		function beforeRename(treeId, treeNode, newName, isCancel) {
			newName = $.trim(newName);
			if (newName.length == 0 || newName.length > 50) {
				var theMsg = "";
				if (newName.length == 0) {
					theMsg = "节点名称不能为空！";
				} else {
					theMsg = "节点名称最多可以输入50个字符！";
				}
				zxxBoxRemind(theMsg,function(){
					var zTree = $.fn.zTree.getZTreeObj("tree");
					setTimeout(function(){zTree.editName(treeNode)}, 10);
				});
				return false;
			}
			return true;
		}
		
		function beforeEditName(treeId, treeNode) {
			if (treeNode.level == 0) {
				return false;
			}
			return true;
		}
		
		var newCount = 1;
		function addHoverDom(treeId, treeNode) {
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
			var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
				+ "' title='add node' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var btn = $("#addBtn_"+treeNode.tId);
			if (btn) btn.bind("click", function(){
				var zTree = $.fn.zTree.getZTreeObj("tree");
				if (treeNode.children && treeNode.children.length > 35) {
					zxxBoxRemind("最多只能添加36级菜单！",null);	
					return false;
				} 
				var noteName = $("#noteName").val();
				if (!noteName) {
					$("#msg").html("");
					zTree.selectNode(treeNode);
					$("#addBox").zxxbox({width:400,btnclose:false});
				} 
				return false;
			});
		};
		
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_"+treeNode.tId).unbind().remove();
		}
		
		function showRemoveBtn(treeId, treeNode) {
			if (treeNode.level == 0) {
				return false;
			}
			return true;
		}

		function selectAll() {
			var zTree = $.fn.zTree.getZTreeObj("tree");
			zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
		}
		
		function dropPrev(treeId, nodes, targetNode) {
			var pNode = targetNode.getParentNode();
			if (pNode && pNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					var curPNode = curDragNodes[i].getParentNode();
					if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}
		
		function dropInner(treeId, nodes, targetNode) {
			if (targetNode && targetNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					if (!targetNode && curDragNodes[i].dropRoot === false) {
						return false;
					} else if (curDragNodes[i].parentTId && curDragNodes[i].getParentNode() !== targetNode && curDragNodes[i].getParentNode().childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}
		
		function dropNext(treeId, nodes, targetNode) {
			var pNode = targetNode.getParentNode();
			if (pNode && pNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					var curPNode = curDragNodes[i].getParentNode();
					if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}
		
		function beforeDrag(treeId, treeNodes) {
			for (var i=0,l=treeNodes.length; i<l; i++) {
				if (treeNodes[i].drag === false) {
					curDragNodes = null;
					return false;
				} else if (treeNodes[i].parentTId && treeNodes[i].getParentNode().childDrag === false) {
					curDragNodes = null;
					return false;
				}
			}
			curDragNodes = treeNodes;
			return true;
		}
		
		
		$(document).ready(function(){
			$.fn.zTree.init($("#tree"), setting, zNodes);
			$("#selectAll").bind("click", selectAll);
		});
		
		function addParentNode() {
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var description = $.trim($("#description").val());
			if (!description) {
				zxxBoxRemind("请输入根节点内容！",null);
				return;
			}
			var newNode = {name:description};
			newNode = treeObj.addNodes(null, newNode);
			$("#description").val("");
			zxxBoxRemind("添加成功！",null);
		}
		
		function zxxBoxRemind(message, event) {
			$.zxxbox.remind(message, event, {
				title : "提示：",
				width : 400,
				bgclose : true,
				btnclose : false,
				drag : true
			});
		}
		
		function operateSub() {
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.transformToArray(treeObj.getNodes());
			var myArray = new Array();
			for(var i=0;i< nodes.length;i++){
				var value = nodes[i].id + "\u0001" +nodes[i].name + "\u0001" + nodes[i].pId + "\u0001" + nodes[i].level + "\u0001" + nodes[i].db_id;
				myArray.push(value);
			}
			
			$.ajax({
				url : "admin/adminoperateModule.action",
				data: {"info":myArray.join("#####")},
				dataType : "json",
				type : "post",
				async : false,
				success : function(data) {
					var resultCode = data.RESULT_CODE;
					if (resultCode == 0) {
						zxxBoxRemind("操作成功！",function() {
							var path = $("base").attr("href");
							location.href = path + "admin/eeModuleManage.jsp";
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