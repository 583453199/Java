package com.zjj.comet4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;

import com.zjj.util.common.Constant;

public class InitTalkListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent arg0) {
		CometContext cc = CometContext.getInstance();
		CometEngine engine = cc.getEngine();
		cc.registChannel(Constant.CHANNEL);
		
		engine.addConnectListener(new JoinTalkListener());
		engine.addDropListener(new LeaveTalkListener());
		
		Thread helloAppModule = new Thread(new TalkSender(), "Talk Room");
		helloAppModule.setDaemon(true);
		helloAppModule.start();
	}

	public void contextDestroyed(ServletContextEvent arg0) {

	}

}
