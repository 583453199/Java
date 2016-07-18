package com.zjj.dao.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.CollectionUtils;

import com.zjj.bean.ShiroResourceBean;
import com.zjj.bean.UserBean;
import com.zjj.dao.ShiroDao;


public class ShiroDaoImpl extends HibernateDaoSupport implements ShiroDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ShiroResourceBean> queryProtectedResourceList() {
		Session session = getSessionFactory().openSession();
		Query query = session.createSQLQuery("select id,type,value,description,url,status from shiro_resource");
		query.setResultTransformer(Transformers.aliasToBean(ShiroResourceBean.class));
		List<ShiroResourceBean> list = query.list();
		session.close();
		return list;
	}
	
	 @Override
	public UserBean queryUserById(int id) {
		Session session = getSessionFactory().openSession();
		UserBean userBean = (UserBean) session.get(UserBean.class, id);
		session.close();
		return userBean;
	}
	 
	@SuppressWarnings("unchecked")
	@Override
	public Set<String> queryUserRoles(String userId) {
		Session session = getSessionFactory().openSession();
		StringBuilder sql = new StringBuilder();
		sql.append("select t2.role_id");
		sql.append(" from user as t1,shiro_user_role as t2");
		sql.append(" where t1.user_id=t2.user_id");
		sql.append(" and t1.user_id = " + userId);
		sql.append(" and t2.status = 0");
		Query query = session.createSQLQuery(sql.toString());
		List<Object> list = query.list();
		session.close();
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		Set<String> set = new HashSet<String>(); 
		for (Object obj : list) {
			set.add(String.valueOf(obj));
		}
		return set;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<String> queryMenuButtonResources(Set<String> roles) {
		Session session = getSessionFactory().openSession();
		StringBuilder sql = new StringBuilder();
		sql.append("select t2.value");
		sql.append(" from shiro_permission AS t1,shiro_resource AS t2");
		sql.append(" WHERE t1.resource_id = t2.id");
		sql.append(" and t1.role_id in (:roleIds)"); 
		sql.append(" and t1.status = 0");
		sql.append(" and t2.status = 0");
		
		Query query = session.createSQLQuery(sql.toString());
		query.setParameterList("roleIds", roles);
		List<Object> list = query.list();
		session.close();
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		Set<String> resourceSet = new HashSet<String>(); 
		for (Object obj : list) {
			resourceSet.add(String.valueOf(obj));
		}
		return resourceSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Set<String>> queryResourceAndRoleMap() {
		Session session = getSessionFactory().openSession();
		StringBuilder sql = new StringBuilder();
		sql.append(" select t1.resource_id,GROUP_CONCAT(t1.role_id) as role_ids");
		sql.append(" from shiro_permission AS t1,shiro_resource AS t2");
		sql.append(" WHERE t1.resource_id = t2.id");
		sql.append(" and t1.status = 0");
		sql.append(" and t2.status = 0");
		sql.append(" group by t1.resource_id");
		Query query = session.createSQLQuery(sql.toString());
		List<Object[]> list = query.list();
		session.close();
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		Map<String, Set<String>> resourceIdRolesMap = new HashMap<String, Set<String>>();
		for (Object[] array : list) {
			String resourceId = array[0].toString();
			String roleIds = array[1].toString();
			String[] roleArr = roleIds.split(",");
			Set<String> roleSet = new HashSet<String>();
			for (String role : roleArr) {
				roleSet.add(role);
			}
			resourceIdRolesMap.put(resourceId, roleSet);
		}
		return resourceIdRolesMap;
	}
}
