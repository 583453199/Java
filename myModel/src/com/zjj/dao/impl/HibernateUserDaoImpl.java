package com.zjj.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.CollectionUtils;

import com.zjj.bean.UserBean;
import com.zjj.dao.UserDao;
import com.zjj.util.common.Page;

public class HibernateUserDaoImpl extends HibernateDaoSupport implements UserDao {
	
	private static final Log log = LogFactory.getLog(HibernateUserDaoImpl.class);
	
	@Override
	public boolean login(String account, String password) {
		Session session = getSessionFactory().openSession();
		boolean operateStatus = this.userSQLQuery(session, account, password);
//		boolean operateStatus = this.userHqlQuery(session, account, password);
		session.close();
		return operateStatus;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public UserBean queryUserByInfo(String account, String password) {
		Session session = getSessionFactory().openSession();
		Query query = session.createSQLQuery("select user_id as userId,user_name as userName,account,password,user_type as userType from user where account=:account and password=:password");
		query.setParameter("account", account);
		query.setParameter("password", password);
		query.setResultTransformer(Transformers.aliasToBean(UserBean.class));
		List<UserBean> list = query.list();
		session.close();
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public boolean userHqlQuery(Session session, String account, String password) {
		Query query = session.createQuery("select new UserBean(account,userName,password) from UserBean where account=:account and password=:password");
		query.setParameter("account", account);
		query.setParameter("password", password);
		List<UserBean> list = query.list();
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		for (UserBean user : list) {
			System.out.println("HQL：" + user.getAccount() + "---" + user.getUserName() + "---" + user.getPassword());
		}
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean userSQLQuery(Session session, String account, String password) {
		Query query = session.createSQLQuery("select user_id as userId,user_name as userName,account,password from user where account=:account and password=:password");
		query.setParameter("account", account);
		query.setParameter("password", password);
		query.setResultTransformer(Transformers.aliasToBean(UserBean.class));
		List<UserBean> list = query.list();
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		for (UserBean user : list) {
			System.out.println("SQL：" +user.getAccount() + "---" + user.getUserName() + "---" + user.getPassword());
		}
		return true;
	}
	
	@Override
	public String addUser(UserBean userBean) {
		try {
			Session session = getSessionFactory().openSession();
			Transaction tx = session.beginTransaction();
			session.save(userBean);
			tx.commit();
			session.close();
		} catch (Exception e) {
			log.error("保存用户信息失败！" + e);
		}
		String userId = String.valueOf(userBean.getUserId());
		return userId;
	}
	
	/**
	 * 查询总数
	 * 
	 * @param requestMap
	 * @return
	 */
	private int queryUserTotalNum(Map<String, String> requestMap) {
		String account = requestMap.get("account");
		String userType = requestMap.get("userType");

		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from UserBean where 1=1 ");
		if (StringUtils.isNotBlank(account)) {
			sql.append(" and (account=:account or user_id=:account)");
		}
		if (StringUtils.isNotBlank(userType)) {
			sql.append(" and userType=:userType");
		}
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(sql.toString());
		if (StringUtils.isNotBlank(account)) {
			query.setParameter("account", account);
		}
		if (StringUtils.isNotBlank(userType)) {
			query.setParameter("userType", Integer.parseInt(userType));
		}
		String total =  String.valueOf(query.uniqueResult());
		return Integer.parseInt(total);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryUserList(Map<String, String> requestMap, Page page) {
		int totalNum = this.queryUserTotalNum(requestMap);
		if (totalNum == 0) {
			return null;
		}
		
		String account = requestMap.get("account");
		String userType = requestMap.get("userType");
		StringBuilder sql = new StringBuilder();
		sql.append("From UserBean where 1=1 ");
		if (StringUtils.isNotBlank(account)) {
			sql.append(" and (account=:account or user_id=:account)");
		}
		if (StringUtils.isNotBlank(userType)) {
			sql.append(" and userType=:userType");
		}

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(sql.toString());
		if (StringUtils.isNotBlank(account)) {
			query.setParameter("account", account);
		}
		if (StringUtils.isNotBlank(userType)) {
			query.setParameter("userType", Integer.parseInt(userType));
		}
		if (page != null) {
			query.setFirstResult(page.getOffset());
			query.setMaxResults(page.getPageSize());
		}
		List<UserBean> list = query.list();
		session.close();
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LIST", list);
		map.put("TOTAL_NUM", totalNum);
		return map;
	}
	
}
