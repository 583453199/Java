package com.zjj.comet4j;

import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;

import com.zjj.comet4j.bean.DataInfo;
import com.zjj.util.common.Constant;

public class TalkSender implements Runnable {

	private static final CometEngine engine = CometContext.getInstance().getEngine();
	private static final DataInfo info = new DataInfo();


	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Integer connectorCount = Integer.valueOf(engine.getConnections().size());
			info.setConnectorCount(connectorCount.toString());
			engine.sendToAll(Constant.CHANNEL, info);
		}
	}

}
