package com.zjj.shiro;

import org.apache.shiro.subject.Subject;

public class LoginSubject {
	private String loginName;

	private String passwordMD5;
	private Subject subject;

	public LoginSubject(String loginName, String passwordMD5, Subject subject) {
		this.loginName = loginName;
		this.passwordMD5 = passwordMD5;
		this.subject = subject;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getPasswordMD5() {
		return passwordMD5;
	}

	public Subject getSubject() {
		return subject;
	}
}
