package com.zjj.bean;

import java.io.Serializable;

public class ShiroUserRoleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4399672889999542578L;

	private int id;
	private int userId;
	private int roleId;
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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
