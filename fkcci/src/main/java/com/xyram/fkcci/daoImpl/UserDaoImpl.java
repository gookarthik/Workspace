package com.xyram.fkcci.daoImpl;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xyram.fkcci.dao.UserDao;
import com.xyram.fkcci.model.UserLogin;
import com.xyram.fkcci.util.AESencryption;
import com.xyram.fkcci.util.CommonUtils;
import com.xyram.fkcci.util.SendMail;
/**
 * 
 * @fileName : UserDaoImpl.java
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
@Repository
@Transactional
public class UserDaoImpl implements UserDao {

	private static final Logger logger = Logger.getLogger(UserDaoImpl.class.getName());

	@Autowired
	private SessionFactory sessionFactory;

	public UserLogin addUser(UserLogin user) throws Exception {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			String email = user.getEmail();
			user.setEmail(email.toLowerCase());
			session.save(user);
			logger.info("user saved successfully, User Details="+ user.getUserId());
			return user;
		} catch (ConstraintViolationException c) {
			throw new EntityExistsException("Email already registered!");
		}
	}

	public UserLogin updateUser(UserLogin user) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(user);
		logger.info("user updated successfully, User Details="+ user.getUserId());
		return user;
	}

	@SuppressWarnings("unchecked")
	public List<UserLogin> getAllUsers() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserLogin.class);
		List<UserLogin> usersList = criteria.list();
		return usersList;
	}

	public UserLogin validateLogin(String email, String password)
			throws Exception {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria c = session.createCriteria(UserLogin.class);
		c.add(Restrictions.eq("email", email.toLowerCase()));
		if (password != null) {
			c.add(Restrictions.eq("password", password));
		}
		UserLogin user = (UserLogin) c.uniqueResult();
		if (user != null) {
			return user;
		} else if (user == null && password == null) {
			throw new EntityNotFoundException("User is not registered!");
		} else {
			throw new EntityNotFoundException("Please check Email and Password!");
		}
	}

	public UserLogin getUserById(Integer id) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria c = session.createCriteria(UserLogin.class);
		c.add(Restrictions.eq("userId", id));
		return (UserLogin) c.uniqueResult();
	}

	public String deleteUserById(Integer id) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserLogin.class);
		criteria.add(Restrictions.eq("userId", id));
		List<UserLogin> list = criteria.list();
		if (list.size() > 0) {
			Iterator it = list.iterator();
			while(it.hasNext()) {
				UserLogin userLogin = (UserLogin) it.next();
				session.delete(userLogin);
			}
		}
		return "user deleted successfully!";
	}

	public UserLogin changePassword(Integer userId, String password)throws Exception {
		String hql = "UPDATE UserLogin set password = :password "+"WHERE user_id = :user_id";
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setParameter("password", AESencryption.encrypt(password));
		query.setParameter("user_id", userId);
		int result = query.executeUpdate();
		System.out.println("Rows affected: " + result);
		return getUserById(userId);
	}

	public String forgotPassword(String email) throws Exception {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria c = session.createCriteria(UserLogin.class);
		c.add(Restrictions.eq("email", email));
		UserLogin user = (UserLogin) c.uniqueResult();
		if (user != null) {
			String randomPass = CommonUtils.generateRandomString(8);
			String hql = "UPDATE UserLogin SET  password = '" + randomPass
					+ "'  WHERE userId =" + user.getUserId();
			Query query = session.createQuery(hql);
			int result = query.executeUpdate();
			System.out.println("UserLogin Update Status=" + result);
			System.out.println("Rows affected: " + result);
			String subject = "Account Modification";
			String bodyText = "Your FKCCI User password has been reset to "+ randomPass
					+ ", please login and change your password for security purposes.";
			SendMail.sendEmail(user.getEmail(), subject, bodyText, null);
			return bodyText;
		} else {
			throw new Exception("email is not Registered with us!!!.");
		}
	}
}
