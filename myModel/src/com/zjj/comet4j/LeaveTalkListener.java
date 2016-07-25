package com.zjj.comet4j;

import org.comet4j.core.CometConnection;
import org.comet4j.core.CometEngine;
import org.comet4j.core.event.DropEvent;
import org.comet4j.core.listener.DropListener;

import com.zjj.comet4j.bean.TalkLeave;
import com.zjj.util.common.Constant;

public class LeaveTalkListener extends DropListener {

	@Override
	public boolean handleEvent(DropEvent event) {
		CometConnection conn = event.getConn();
		if (conn != null) {
			String userName = AppStore.getInstance().get(conn.getId());
			TalkLeave leave = new TalkLeave(conn.getId(), userName);
			AppStore.getInstance().getMap().remove(conn.getId());
			((CometEngine) event.getTarget()).sendToAll(Constant.CHANNEL, leave);
		}
		return true;
	}

}
