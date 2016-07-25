package com.zjj.comet4j.bean;

import com.zjj.util.common.DateUtil;

public class TalkJoin {
	private final String transtime;
	private String type;
	private String id;
	private String name;

	public TalkJoin(String id, String name) {
		this.type = "join";
		this.id = id;
		this.name = name;
		this.transtime = DateUtil.getCurrentDateStr("HH:mm");
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTranstime() {
		return this.transtime;
	}
}
