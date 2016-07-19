<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="zjj" uri="http://10.204.42.36/s2shModel/tags"%>
<div class="adminLeft list">
	<ul class="yiji">
		<li><a href="/admin/adminIndex.action">个人中心</a></li>
		<li><a href="javascript:;" class="inactive">权限管理</a>
			<ul style="display: none">
				<li><a href="javascript:;" class="inactive active">用户管理</a>
					<ul>
						<li><a href="/admin/userManage.action" rel="userManage">用户查询</a></li>
						<li><a href="/admin/userAdd.jsp" rel="userAdd">用户新增</a></li>
					</ul>
				</li> 
				<li><a href="/admin/roleManage.action" rel="roleManage">角色管理</a></li> 
				<li class="last"><a href="/admin/resourceManage.action" rel="resourceManage">资源管理</a></li> 
			</ul>
		</li>
		<%--<li><a href="javascript:;" class="inactive">组织机构</a>
			<ul style="display: none">
				<li><a href="javascript:;" class="inactive active">美协机关</a>
					<ul>
						<li><a href="javascript:;">办公室</a></li>
						<li><a href="javascript:;">人事处</a></li>
						<li><a href="javascript:;">组联部</a></li>
						<li><a href="javascript:;">外联部</a></li>
						<li><a href="javascript:;">研究部</a></li>
						<li><a href="javascript:;">维权办</a></li>
					</ul>
				</li> 
				<li><a href="javascript:;" class="inactive active">中国文联美术艺术中心</a>   
					<ul>
						<li><a href="javascript:;">综合部</a></li>
						<li><a href="javascript:;">大型活动部</a></li>
						<li><a href="javascript:;">展览部</a></li>
						<li><a href="javascript:;">艺委会工作部</a></li>
						<li><a href="javascript:;">信息资源部</a></li>
						<li><a href="javascript:;">双年展办公室</a></li>
					</ul>
				</li> 
				<li class="last"><a href="javascript:;">《美术》杂志社</a></li> 
			</ul>
		</li>
	--%>
	</ul>
</div>
