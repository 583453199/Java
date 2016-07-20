package com.zjj.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.zjj.bean.ShiroResourceBean;
import com.zjj.service.ShiroResourceService;
import com.zjj.service.ShiroRoleService;
import com.zjj.util.CommonUtil;
import com.zjj.util.SessionUtil;
import com.zjj.util.common.DateUtil;

public class AdminAsynAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ShiroResourceService resourceService;
	
	private ShiroRoleService roleService;
	
	private String resultCode;
	
	private boolean statusCode;
	
	private Map<String, Object> map;
	
	private String url;
	
	private String description;
	
	private String value;
	
	private String type;
	
	private String id;
	
	private String parentId;
	
	/**
	 * 资源新增
	 * 
	 * @return
	 */
	public String addResourceSubmit() {
		if (StringUtils.isBlank(parentId)) {
			parentId = "";
		}
		List<Map<String, Object>> resourceList = resourceService.queryAllResourceByLevel(parentId.length() + 1, parentId);
		String[] sortArr = CommonUtil.getCharArray();
		int num = 0;
		if (CollectionUtils.isNotEmpty(resourceList)) {
			num = resourceList.size();
		}
		// 超出最大限制
		if (num > sortArr.length) {
			statusCode = false;
			return Action.SUCCESS;
		}
		ShiroResourceBean bean = new ShiroResourceBean();
		bean.setUrl(url);
		bean.setType(Integer.parseInt(type));
		bean.setValue( parentId + sortArr[num]);
		bean.setDescription(description);
		String resourceId = resourceService.addShiroResource(bean);
		statusCode = StringUtils.isNotBlank(resourceId);
		return Action.SUCCESS;
	}

	/**
	 * 角色新增
	 * 
	 * @return resultCode: 0、成功  1、失败  2、角色已存在
	 */
	public String addRole() {
		Map<String, Object> roleMap = roleService.queryRoleByNameOrId(description, null);
		if (MapUtils.isNotEmpty(roleMap)) {
			resultCode = "2";
			return Action.SUCCESS;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("description", description);
		paramMap.put("create_user", SessionUtil.getUserSessionInfo("USER_ID"));
		paramMap.put("create_time", DateUtil.getCurrentDateTime());
		paramMap.put("builtin", "1");
		boolean executeStatus = roleService.addRole(paramMap);
		resultCode = executeStatus ? "0" : "1";
		return Action.SUCCESS;
	}
	
	
	/**
	 * 加载资源信息 - 角色授权
	 * 
	 * @return
	 */
	public String loadResource() {
		
		return Action.SUCCESS;
	}
	
	@JSON(serialize = false)
	public ShiroResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ShiroResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@JSON(serialize = false)
	public ShiroRoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(ShiroRoleService roleService) {
		this.roleService = roleService;
	}
	
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isStatusCode() {
		return statusCode;
	}

	public void setStatusCode(boolean statusCode) {
		this.statusCode = statusCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}
