package com.zjj.action;

import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.zjj.comet4j.AppStore;
import com.zjj.comet4j.bean.Talk;
import com.zjj.comet4j.bean.TalkRandom;
import com.zjj.util.common.Constant;

public class TalkRoomAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private static final CometContext context = CometContext.getInstance();
	// private static final CometEngine engine = context.getEngine();
	// private static final AppStore appStore = AppStore.getInstance();
	private String type;
	private String id;
	private String text;
	private String newName;
	private String oldName;

	public String talk() {
		AppStore appStore = AppStore.getInstance();
		CometContext cc = CometContext.getInstance();
		CometEngine engine = cc.getEngine();

		if ("talk".equals(type)) {
			String name = appStore.get(id);
			Talk talk = new Talk(id, name, text);
			engine.sendToAll(Constant.CHANNEL, talk);
		}
		if ("rename".equals(type)) {
			appStore.put(id, newName);
			TalkRandom dto = new TalkRandom(id, oldName, newName);
			engine.sendToAll("talker", dto);
		}
		return Action.SUCCESS;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

}
