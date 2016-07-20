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
<title>管理员后台 - 资源编辑</title>
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
	.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
</style>
</head>
<body id="resourceEdit">
	<jsp:include page="/admin/adminTop.jsp"></jsp:include>
	<div class="adminContent">
		<jsp:include page="/admin/adminLeft.jsp"/>
		<div class="rightContent">
			<form action="admin/toResourceEdit.action" id="form1" method="post">
				<ul id="tree" class="ztree"></ul>
				<a href="javascript:;" class="my_button color03 subAdd" style="width: 50px;margin-left: 50px;" onclick="operateSub()">提交</a>
			</form>
		</div>
	</div>
	
	<div id="addBox" style="display:none;">
	  <div style="margin: 20px;">
	   	<p id="city_msgTips" class="mb_10"  style="color:#FA9652;"></p>
	       <div class="tc mb_10" style="float: left;"  id="protection">
	       	  <p id="msg" class="red"></p>
	       	  <div>菜单描述：<input type="text" id="description" class="input01" maxlength="20"/></div><br/>
	       	  <div>菜单地址：<input type="text" id="url" class="input01" maxlength="100"/></div>
	       </div>
	       <div style="clear :left;">
		       	  <a href="javascript:void(0)" class="x_button w_50 mt_10" onclick="addNoteSub()">确定</a>&nbsp;&nbsp;
		       	  <a id="cancelBtn" class="x_button w_50 mt_10" href="javascript:hideZxxBox();">取消</a>
	       </div>
	   </div>
	</div>
	<script type="text/javascript" src="/js/jquery.zxxbox.3.0.js"></script>
	<script type="text/javascript" src="/js/ztree/jquery.ztree.core.custom.js"></script>
	<script type="text/javascript" src="/js/ztree/jquery.ztree.excheck.min.js"></script>
	<script type="text/javascript" src="/js/ztree/jquery.ztree.exedit.min.js"></script>
	
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
	
		var data = '<s:property  value="content" escapeHtml="false" />';
		var zNodes = $.parseJSON(data);
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
				var description = $("#description").val();
				if (!description) {
					$("#msg").html("");
					zTree.selectNode(treeNode);
					$("#addBox").zxxbox({title:"新增菜单",width:400,btnclose:false});
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
		}
		
		function hideZxxBox() {
			$("#description").val("");
			$.zxxbox.hide();
		}
		
		function addNoteSub() {
			var description = $.trim($("#description").val());
			var url = $.trim($("#url").val());
			if (!description) {
				$("#msg").html("菜单名不能为空！");
			} else {
				var treeObj = $.fn.zTree.getZTreeObj("tree");
				var nodes = treeObj.getSelectedNodes();
				var treeNode = nodes[0]; 
				$.ajax({
					url : "adminAsyn/addResourceSubmit.action",
					data: {"type":"1","url":url,"description":description,"parentId":treeNode.id},
					dataType : "json",
					type : "post",
					async : false,
					success : function(data) {
						var resultCode = data.statusCode;
						if (resultCode) {
							zxxBoxRemind("操作成功！",function() {
								$("#form1").submit();
							});
						} else {
							zxxBoxRemind("操作失败！",null);
						}
					}
				})
			}
		}
	</script>
</body>
</html>
