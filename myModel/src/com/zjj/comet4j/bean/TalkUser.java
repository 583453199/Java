package com.zjj.comet4j.bean;

import com.zjj.util.common.DateUtil;

public class TalkUser {
	
	private final String transtime;
	private String id;
	private String name;

	public TalkUser(String id, String name, String text) {
		this.id = id;
		this.name = name;
		this.transtime = DateUtil.getCurrentDateStr("HH:mm");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTranstime() {
		return transtime;
	}
}
