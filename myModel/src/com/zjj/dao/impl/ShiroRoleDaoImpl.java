package com.zjj.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.sqlgenerator.Query;
import com.sqlgenerator.SQL;
import com.sqlgenerator.Service;
import com.sqlgenerator.Util;
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
		Query mainQuery = SQL.select("ROLE_ID,DESCRIPTION,STATUS");
		mainQuery.select("DATE_FORMAT(CREATE_TIME,'%Y-%m-%d %H:%i:%s') AS CREATE_TIME");
		mainQuery.from("SHIRO_ROLE");
		mainQuery.where("STATUS=", 0);
		if (StringUtils.isNotBlank(description)) {
			mainQuery.and("DESCRIPTION LIKE", description + "%");
		}
		if (page != null) {
			mainQuery.limit(page.getOffset(), page.getPageSize());
		}
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

	public HibernateBasic getHibernateBasic() {
		return hibernateBasic;
	}

	public void setHibernateBasic(HibernateBasic hibernateBasic) {
		this.hibernateBasic = hibernateBasic;
	}
}
