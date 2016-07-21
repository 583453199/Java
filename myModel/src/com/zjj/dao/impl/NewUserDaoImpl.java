package com.zjj.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sql.generator.GroupCondition;
import com.sql.generator.Insert;
import com.sql.generator.Pref;
import com.sql.generator.Query;
import com.sql.generator.SQL;
import com.sql.generator.Service;
import com.sql.generator.Util;
import com.zjj.bean.UserBean;
import com.zjj.dao.NewUserDao;
import com.zjj.util.CommonUtil;
import com.zjj.util.HibernateBasic;
import com.zjj.util.common.Page;

public class NewUserDaoImpl implements NewUserDao {
	
	private static final Log log = LogFactory.getLog(NewUserDaoImpl.class);
	
	private HibernateBasic hibernateBasic;
	
	@Override
	public boolean login(String account, String password) {
		return false;
	}
	
	@Override
	public UserBean queryUserByInfo(String account, String password) {
//		Session session = getSessionFactory().openSession();
//		Query query = session.createSQLQuery("select user_id as userId,user_name as userName,account,password,user_type as userType from user where account=:account and password=:password");
//		query.setParameter("account", account);
//		query.setParameter("password", password);
//		query.setResultTransformer(Transformers.aliasToBean(UserBean.class));
//		List<UserBean> list = query.list();
//		session.close();
//		if (CollectionUtils.isEmpty(list)) {
//			return null;
//		}
//		return list.get(0);
		return null;
	}
	
	public boolean userHqlQuery(String account, String password) {
//		Query query = session.createQuery("select new UserBean(account,userName,password) from UserBean where account=:account and password=:password");
//		query.setParameter("account", account);
//		query.setParameter("password", password);
//		List<UserBean> list = query.list();
//		if (CollectionUtils.isEmpty(list)) {
//			return false;
//		}
//		for (UserBean user : list) {
//			System.out.println("HQL：" + user.getAccount() + "---" + user.getUserName() + "---" + user.getPassword());
//		}
		return true;
	}
	
	
	public boolean userSQLQuery() {
//		Query query = session.createSQLQuery("select user_id as userId,user_name as userName,account,password from user where account=:account and password=:password");
//		query.setParameter("account", account);
//		query.setParameter("password", password);
//		query.setResultTransformer(Transformers.aliasToBean(UserBean.class));
//		List<UserBean> list = query.list();
//		if (CollectionUtils.isEmpty(list)) {
//			return false;
//		}
//		for (UserBean user : list) {
//			System.out.println("SQL：" +user.getAccount() + "---" + user.getUserName() + "---" + user.getPassword());
//		}
		return true;
	}
	
	@Override
	public String addUser(UserBean userBean) {
		Insert insert = SQL.insert("USER");
		insert.value("ACCOUNT", userBean.getAccount());
		insert.value("USER_NAME", userBean.getUserName());
		insert.value("PASSWORD", userBean.getPassword());
		insert.value("STATUS", 0);
		insert.value("AGE", userBean.getAge());
		insert.value("USER_TYPE", userBean.getUserType());
		
		Service service = new Service(insert);
		service.useTransaction();
		service.setDescription(this.getClass() + ".addRole()");
		Map<String, Object> result = hibernateBasic.execute(service);
		if (CommonUtil.isInvalid(result)) {
			return null;
		}
		String userId = String.valueOf(SQL.lastInsertId(insert));
		return userId;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryUserList(Map<String, String> requestMap, Page page) {
		String account = requestMap.get("account");
		String userType = requestMap.get("userType");

		Query mainQuery = SQL.select("USER_ID,ACCOUNT,USER_NAME,PASSWORD,AGE,USER_TYPE");
		mainQuery.from("USER");
		if (StringUtils.isNotBlank(account)) {
			GroupCondition condition = SQL.groupCondition(Pref.OR);
			condition.append("account=", account);
			condition.append("user_id=", account);
			mainQuery.and(condition);
		}
		if (StringUtils.isNotBlank(userType)) {
			mainQuery.and("USER_TYPE=", userType);
		}
		mainQuery.setGenerateCountQuery(true);
		if (page != null) {
			mainQuery.limit(page.getOffset(), page.getPageSize());
		}

		Service service = new Service(mainQuery);
		service.useCustomResultTransformer();
		service.setDescription(this.getClass() + ".queryUserList()");
		Map<String, Object> result = hibernateBasic.execute(service);
		if (CommonUtil.isInvalid(result)) {
			log.error("执行失败！");
			return null;
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(mainQuery.getResultCallback());
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("LIST", list);
		resultMap.put("TOTAL_NUM", Util.getTotalNumStr(result, mainQuery));
		return resultMap;
	}

	public HibernateBasic getHibernateBasic() {
		return hibernateBasic;
	}

	public void setHibernateBasic(HibernateBasic hibernateBasic) {
		this.hibernateBasic = hibernateBasic;
	}
	
}
