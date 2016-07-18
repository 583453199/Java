package com.zjj.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUtil {
//	private static SessionFactory sessionFactory;

	/**
	 * @return 获取会话工厂
	 */
	public static SessionFactory getSessionFactory() {
//		// 第一步:读取Hibernate的配置文件 hibernamte.cfg.xml文件
//		Configuration con = new Configuration().configure();
//		// 第二步:创建服务注册构建器对象，通过配置对象中加载所有的配置信息
//		ServiceRegistryBuilder regbulider = new ServiceRegistryBuilder().applySettings(con.getProperties());
//		// 创建注册服务
//		ServiceRegistry reg = regbulider.buildServiceRegistry();
//		// 第三步:创建会话工厂
//		SessionFactory sessionFactory = con.buildSessionFactory(reg);
//		return sessionFactory;
		return null;
	}

	/**
	 * @return 获取会话对象
	 */
	public static Session getSession() {
		return getSessionFactory().openSession();
	}
}
