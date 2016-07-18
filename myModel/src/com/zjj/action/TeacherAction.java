package com.zjj.action;

import com.opensymphony.xwork2.ActionSupport;

public class TeacherAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7032861181936643243L;

	public String teacherIndex() {
		System.out.println("AdminAction.adminIndex()");
		return "index";
	}

}