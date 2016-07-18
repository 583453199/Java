package com.zjj.shiro;

import org.apache.shiro.authc.SimpleAuthenticationInfo;

public class AuthenticationInfo extends SimpleAuthenticationInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3789733989206391599L;
	
	private String account;

	public AuthenticationInfo(Object principal, Object credentials, String realmName) {
		super(principal, credentials, realmName);
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}
