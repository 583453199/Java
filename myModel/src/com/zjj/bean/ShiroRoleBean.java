package com.zjj.bean;

import java.io.Serializable;
import java.util.Date;

public class ShiroRoleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4399672889999542578L;


	private int roleId;
	private String description;
	private Date createTime;
	private int createUser;
	private int builtin;
	private int status;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getBuiltin() {
		return builtin;
	}

	public void setBuiltin(int builtin) {
		this.builtin = builtin;
	}
	
}
