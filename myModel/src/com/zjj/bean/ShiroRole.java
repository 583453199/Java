package com.zjj.bean;

import java.io.Serializable;
import java.util.Date;

public class ShiroRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4399672889999542578L;


	private int roleId;
	private String description;
	private Date createTime;
	private String createUser;
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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
