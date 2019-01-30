package com.xyram.fkcci.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 
 * @fileName : AbstractDao.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: Nov 18, 2017
 *
 * @Author: Pradeep Rana
 *
 * @Reviewer: Sateesh Reddy
 */
public abstract class AbstractDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void persist(Object entity) {
		getSession().persist(entity);
	}

	public void delete(Object entity) {
		getSession().delete(entity);
	}
}
