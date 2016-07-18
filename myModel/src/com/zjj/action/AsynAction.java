package com.zjj.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.zjj.bean.ShiroResourceBean;
import com.zjj.service.ShiroResourceService;

public class AsynAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ShiroResourceService sResourceService;
	
	private String resultCode;
	
	private boolean statusCode;
	
	private Map<String, Object> map;
	
	private String url;
	
	private String description;
	
	private String value;
	
	private String id;
	
	/**
	 * 资源新增
	 * 
	 * @return
	 */
	public String addResourceSubmit() {
		ShiroResourceBean bean = new ShiroResourceBean();
		bean.setUrl(url);
		bean.setValue(value);
		bean.setDescription(description);
		String resourceId = sResourceService.addShiroResource(bean);
		statusCode = StringUtils.isNotBlank(resourceId);
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
	public ShiroResourceService getsResourceService() {
		return sResourceService;
	}

	public void setsResourceService(ShiroResourceService sResourceService) {
		this.sResourceService = sResourceService;
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
}
