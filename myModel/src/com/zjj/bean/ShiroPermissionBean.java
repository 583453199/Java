package com.zjj.bean;

import java.io.Serializable;

public class ShiroPermissionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4399672889999542578L;

	private int id;
	private int resourceId;
	private int roleId;
	private int status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getResourceId() {
		return resourceId;
	}
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	
}
