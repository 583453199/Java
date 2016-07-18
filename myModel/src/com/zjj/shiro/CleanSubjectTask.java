package com.zjj.shiro;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.zjj.util.CommonUtil;


/**
 * Session already invalidated 会导致subject.getPrincipal() 和
 * subject.getSession(false).getLastAccessTime() 报错
 * 
 */
public class CleanSubjectTask extends TimerTask {
	private static final Map<String, List<LoginSubject>> LOGIN_SUBJECTS = new HashMap<String, List<LoginSubject>>();

	public static void put(String userId, String loginName, String passwordMD5, Subject subject) {
		LoginSubject loginSubject = new LoginSubject(loginName, passwordMD5, subject);

		List<LoginSubject> subjectList = LOGIN_SUBJECTS.get(userId);
		if (subjectList == null) {
			subjectList = new LinkedList<LoginSubject>();
			LOGIN_SUBJECTS.put(userId, subjectList);
		}

		subjectList.add(loginSubject);
	}

	public static List<LoginSubject> get(String userId) {
		List<LoginSubject> loginSubjectList = LOGIN_SUBJECTS.get(userId);
		return getLoginSubject(loginSubjectList);
	}

	public static List<LoginSubject> remove(String userId) {
		List<LoginSubject> loginSubjectList = LOGIN_SUBJECTS.remove(userId);
		return getLoginSubject(loginSubjectList);
	}

	private static List<LoginSubject> getLoginSubject(List<LoginSubject> loginSubjectList) {
		if (loginSubjectList == null) {
			return null;
		}
		List<LoginSubject> result = new LinkedList<LoginSubject>();
		for (LoginSubject loginSubject : loginSubjectList) {
			LoginSubject newLoginSubject = new LoginSubject(loginSubject.getLoginName(), loginSubject.getPasswordMD5(),
					loginSubject.getSubject());
			result.add(newLoginSubject);
		}
		return result;
	}

	public void run() {
		Iterator<Map.Entry<String, List<LoginSubject>>> mapIter = LOGIN_SUBJECTS.entrySet().iterator();
		while (mapIter.hasNext()) {
			Map.Entry<String, List<LoginSubject>> entry = mapIter.next();
			String userId = entry.getKey();
			List<LoginSubject> loginSubjectList = entry.getValue();

			Iterator<LoginSubject> loginSubjectIter = loginSubjectList.iterator();
			while (loginSubjectIter.hasNext()) {
				LoginSubject loginSubject = loginSubjectIter.next();
				Subject subject = loginSubject.getSubject();
				if (subject == null) {
					loginSubjectIter.remove();
					continue;
				}
				Session session = subject.getSession(false);
				if (session == null) {
					doRemoveLoginSubject(loginSubjectIter, userId, null);
					continue;
				}
				long inactiveInterval = 0;
				long timeout = 1;
				try {
					inactiveInterval = session.getLastAccessTime().getTime() - session.getStartTimestamp().getTime();
					timeout = session.getTimeout();
				} catch (Exception e) { // 会话已经过期
					doRemoveLoginSubject(loginSubjectIter, userId, null);
					continue;
				}
				if (inactiveInterval >= timeout) {
					doRemoveLoginSubject(loginSubjectIter, userId, session);
				}
			}

			if (loginSubjectList.isEmpty()) {
				mapIter.remove();
			}
		}
	}

	public void doRemoveLoginSubject(Iterator<LoginSubject> loginSubjectIter, String userId, Session session) {
		try {
			CommonUtil.cleanShiroCache(userId);
			if (session != null) {
				session.stop(); // 使会话过期
			}
		} catch (Exception e) {
		} finally {
			loginSubjectIter.remove();
		}
	}
	
}
