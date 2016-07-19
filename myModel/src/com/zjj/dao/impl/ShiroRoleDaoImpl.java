package com.zjj.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.sql.generator.Insert;
import com.sql.generator.Query;
import com.sql.generator.SQL;
import com.sql.generator.Service;
import com.sql.generator.Util;
import com.zjj.dao.ShiroRoleDao;
import com.zjj.util.CommonUtil;
import com.zjj.util.HibernateBasic;
import com.zjj.util.common.Page;

public class ShiroRoleDaoImpl implements ShiroRoleDao {

	private HibernateBasic hibernateBasic;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryRoleList(Map<String, String> requestMap, Page page) {
		String description = (String) requestMap.get("description");
		
		Query mainQuery = SQL.select("ROLE_ID,DESCRIPTION,STATUS,BUILTIN");
		mainQuery.select("DATE_FORMAT(CREATE_TIME,'%Y-%m-%d %H:%i:%s') AS CREATE_TIME");
		mainQuery.from("SHIRO_ROLE");
		mainQuery.where("STATUS=", 0);
		if (StringUtils.isNotBlank(description)) {
			mainQuery.and("DESCRIPTION LIKE", description + "%");
		}
		if (page != null) {
			mainQuery.limit(page.getOffset(), page.getPageSize());
		}
		mainQuery.orderBy("ROLE_ID DESC");
		mainQuery.setGenerateCountQuery(true);

		Service service = new Service(mainQuery);
		service.useCustomResultTransformer();
		service.setDescription(this.getClass() + ".queryPermissionList()");
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
	public Map<String, Object> queryRoleByNameOrId(String name, String roleId) {
		Query mainQuery = SQL.select("ROLE_ID,DESCRIPTION,STATUS,BUILTIN");
		mainQuery.from("SHIRO_ROLE");
		if (StringUtils.isNotBlank(name)) {
			mainQuery.and("DESCRIPTION=", name);
		}
		if (StringUtils.isNotBlank(roleId)) {
			mainQuery.and("ROLE_ID=", roleId);
		}
		mainQuery.limit(1);

		Service service = new Service(mainQuery);
		service.useCustomResultTransformer();
		service.setDescription(this.getClass() + ".queryRoleByNameOrId()");
		Map<String, Object> result = hibernateBasic.execute(service);
		if (CommonUtil.isInvalid(result)) {
			return null;
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(mainQuery.getResultCallback());
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public boolean addRole(Map<String, Object> paramMap) {
		String createUser = (String) paramMap.get("create_user");
		String createTime = (String) paramMap.get("create_time");
		String description = (String) paramMap.get("description");
		String builtin = (String) paramMap.get("builtin");
		
		Insert insert = SQL.insert("shiro_role");
		insert.value("create_user", createUser);
		insert.value("create_time", createTime);
		insert.value("description", description);
		insert.value("status", 0);
		insert.value("builtin", builtin);
		
		Service service = new Service(insert);
		service.useCustomResultTransformer();
		service.setDescription(this.getClass() + ".addRole()");
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
