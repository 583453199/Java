package com.zjj.comet4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.comet4j.core.CometConnection;
import org.comet4j.core.CometEngine;
import org.comet4j.core.event.ConnectEvent;
import org.comet4j.core.listener.ConnectListener;

import com.zjj.comet4j.bean.TalkJoin;
import com.zjj.util.common.Constant;

public class JoinTalkListener extends ConnectListener {

	@Override
	public boolean handleEvent(ConnectEvent event) {
		CometConnection conn = event.getConn();
		HttpServletRequest request = conn.getRequest();
		String userName = getCookieValue(request.getCookies(), "userName", "");
		try {
			userName = URLDecoder.decode(userName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		TalkJoin join = new TalkJoin(conn.getId(), userName);
		AppStore.getInstance().put(conn.getId(), userName);
		((CometEngine) event.getTarget()).sendToAll(Constant.CHANNEL, join);
		return true;
	}

	public String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
		String result = defaultValue;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookieName.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return result;
	}
}
