package com.zjj.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sql.generator.Insert;
import com.sql.generator.Query;
import com.sql.generator.ResultColumnValue;
import com.sql.generator.SQL;
import com.sql.generator.Service;
import com.sql.generator.Update;
import com.sql.generator.Util;
import com.zjj.bean.ShiroResourceBean;
import com.zjj.dao.ShiroResourceDao;
import com.zjj.util.CommonUtil;
import com.zjj.util.HibernateBasic;
import com.zjj.util.common.Page;

public class ShiroResourceDaoImpl implements ShiroResourceDao {
	
	private static final Log log = LogFactory.getLog(ShiroResourceDaoImpl.class);
	
	private HibernateBasic hibernateBasic;

	@SuppressWarnings("unchecked")
	@Override
	public String addShiroResource(ShiroResourceBean bean) {
		Insert insert = SQL.insert("shiro_resource");
		insert.value("type", bean.getType());
		insert.value("value", bean.getValue());
		insert.value("description", bean.getDescription());
		insert.value("url", bean.getUrl());
		insert.value("status", 0);

		Service service = new Service(insert);
		service.setDescription(this.getClass() + ".addShiroResource()");
		Map<String, Object> result = hibernateBasic.execute(service);
		if (CommonUtil.isInvalid(result)) {
			log.error("添加资源失败！");
			return null;
		}
		Map<String, Object> resMap = (Map<String, Object>) result.get(insert.getResultCallback());
		return resMap.get(ResultColumnValue.KEY_LAST_INSERT_ID) + "";
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryResourceList(Map<String, String> requestMap, Page page) {
		String description = requestMap.get("description");
		String url = requestMap.get("url");
		Query mainQuery = SQL.select("ID,TYPE,VALUE,DESCRIPTION,URL,STATUS");
		mainQuery.from("shiro_resource");
		if (StringUtils.isNotBlank(description)) {
			mainQuery.and("description like", "%" + description + "%");
		}
		if (StringUtils.isNotBlank(url)) {
			mainQuery.and("url like", "%" + url + "%");
		}

		if (page != null) {
			mainQuery.limit(page.getOffset(), page.getPageSize());
		} else {
			mainQuery.setProhibitDefaultLimit(true);
		}
		mainQuery.setGenerateCountQuery(true);

		Service service = new Service(mainQuery);
		service.useCustomResultTransformer();
		service.setDescription(this.getClass() + ".queryResourceList()");
		Map<String, Object> result = hibernateBasic.execute(service);
		if (CommonUtil.isInvalid(result)) {
			return null;
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(mainQuery.getResultCallback());
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("LIST", list);
		resultMap.put("TOTAL_NUM", Util.getTotalNumStr(result, mainQuery));
		return resultMap;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryRoleResourceList(String roleId) {
		Query mainQuery = SQL.select("if(substring(value,1,length(value)-1) = '',0,substring(value,1,length(value)-1)) as pId");
		mainQuery.select("value as id,description as name,'true' as 'open',length(value) as 'level',id as db_id");
		mainQuery.from("shiro_resource");
		mainQuery.where("status=", 0);
		mainQuery.setProhibitDefaultLimit(true);
		
		Query permissionQuery = SQL.select("id as permission_id,resource_id,'true' as 'checked'");
		permissionQuery.from("shiro_permission");
		permissionQuery.where("status=", 0);
		permissionQuery.and("role_id=", roleId);
		permissionQuery.and("resource_id in", SQL.resultColumnValue(mainQuery, "db_id", -1));

		Service service = new Service(mainQuery, permissionQuery);
		service.addMergeRule(SQL.mergeRule(permissionQuery, mainQuery, "resource_id", "db_id"));
		service.useCustomResultTransformer();
		service.setDescription(this.getClass() + ".queryRoleResourceList()");
		
		Map<String, Object> result = hibernateBasic.execute(service);
		if (CommonUtil.isInvalid(result)) {
			return null;
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(mainQuery.getResultCallback());
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list;
	}
	
	@Override
	public boolean updatePermissionByRoleId(String roleId) {
		Update update = SQL.update("SHIRO_PERMISSION");
		update.set("STATUS", 1);
		update.where("ROLE_ID=", roleId);
		
		Service service = new Service(update);
		service.setDescription(this.getClass() + ".updatePermissionByRoleId()");
		Map<String, Object> result = hibernateBasic.execute(service);
		if (CommonUtil.isInvalid(result)) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryPermissionByRoleId(String roleId) {
		Query mainQuery = SQL.select("ID,RESOURCE_ID,STATUS,ROLE_ID");
		mainQuery.from("SHIRO_PERMISSION");
		mainQuery.where("ROLE_ID=", roleId);
		mainQuery.setProhibitDefaultLimit(true);
		
		Service service = new Service(mainQuery);
		service.useCustomResultTransformer();
		service.setDescription(this.getClass() + ".queryPermissionByRoleId()");
		Map<String, Object> result = hibernateBasic.execute(service);
		if (CommonUtil.isInvalid(result)) {
			return null;
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(mainQuery.getResultCallback());
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list;
	}
	
	@Override
	public boolean updatePermissionInfo(List<String> updateList, List<String> addList, String roleId) {
		// 将DB角色-资源数据状态全部修改已删除
		this.updatePermissionByRoleId(roleId);

		Service service = new Service();
		if (CollectionUtils.isNotEmpty(updateList)) {
			for (String resourceId : updateList) {
				Update update = SQL.update("SHIRO_PERMISSION");
				update.set("STATUS", 0);
				update.where("ROLE_ID=", Integer.parseInt(roleId));
				update.and("RESOURCE_ID=", resourceId);
				service.addGeneratable(update);
			}
		}
		if (CollectionUtils.isNotEmpty(addList)) {
			for (String resourceId : addList) {
				Insert insert = SQL.insert("SHIRO_PERMISSION");
				insert.value("STATUS", 0);
				insert.value("RESOURCE_ID", resourceId);
				insert.value("ROLE_ID", roleId);
				service.addGeneratable(insert);
			}
		}
		service.setDescription(this.getClass() + ".updatePermissionInfo()");
		service.useTransaction();
		Map<String, Object> result = hibernateBasic.execute(service);
		if (CommonUtil.isInvalid(result)) {
			return false;
		}
		return true;
	}
	
	public HibernateBasic getHibernateBasic() {
		return hibernateBasic;
	}

	public void setHibernateBasic(HibernateBasic hibernateBasic) {
		this.hibernateBasic = hibernateBasic;
	}
	
}
