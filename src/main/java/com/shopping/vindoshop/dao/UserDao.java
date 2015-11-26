package com.shopping.vindoshop.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.shopping.vindoshop.model.User;
import com.shopping.vindoshop.model.UserDetail;
import com.shopping.vindoshop.util.CommonUtil;

@Component
public class UserDao {

	@Autowired
	SessionFactory sessionFactory;

	public boolean addUser(User user, Session session) throws Exception {
		boolean userAdded = false;
		Serializable check = null;
		if (user.getPassword() != null)
			user.setPassword(new BCryptPasswordEncoder().encode(user
					.getPassword()));

		check = session.save(user);
		session.flush();
		if (Integer.valueOf(check.toString()) > 0)
			userAdded = true;
		return userAdded;
	}

	public int addUserDetail(UserDetail userDetail, Session session)
			throws Exception {
		int userId = 0;
		userDetail.setUpdatedAt(new Date());
		if (userDetail.getId() == 0) {
			if (userDetail.getUser().getPassword() != null)
				userDetail.getUser().setPassword(
						new BCryptPasswordEncoder().encode(userDetail.getUser()
								.getPassword()));
			userDetail.setReferral(new BigInteger(130, new SecureRandom())
					.toString(32).substring(0, 5).toUpperCase());
			userId = (int) session.save(userDetail);
		} else {
			session.merge(userDetail);
		}
		session.flush();
		return userId;
	}

	@SuppressWarnings("unchecked")
	public List<UserDetail> fetchleaderBoard(User user, Session session)
			throws Exception {
		List<UserDetail> users = new ArrayList<UserDetail>();
		users = session
				.createQuery(
						"Select firstName, lastName, points,0 from UserDetail order by points DESC")
				.setMaxResults(10).list();
		if (user != null) {
			UserDetail currentUser = fetchUserDetail(user, session);
			if (currentUser != null) {
				int rank = ((BigInteger) session
						.createSQLQuery(
								"SELECT FIND_IN_SET( points, (SELECT GROUP_CONCAT( points order by points desc ) FROM userdetail )) AS rank FROM userdetail WHERE id ="
										+ currentUser.getId()).list().get(0))
						.intValue();
				currentUser.setRank(rank);
				users.add(currentUser);
			}
		}
		if (users.size() > 0) {
			return users;
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public UserDetail fetchUserDetail(User user, Session session)
			throws Exception {
		List<UserDetail> users = new ArrayList<UserDetail>();
		users = session.createQuery("from UserDetail where user=?")
				.setParameter(0, user).list();
		if (users.size() > 0) {
			UserDetail userDetail = users.get(0);
			return userDetail;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public String fetchUserName(Session session, User user) throws Exception {
		List<String> users = new ArrayList<String>();
		users = session
				.createQuery("Select firstName from UserDetail where user=?")
				.setParameter(0, user).list();
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public UserDetail findByReferral(String referral, Session session)
			throws Exception {
		if (session == null)
			session = CommonUtil.getSession(sessionFactory);
		List<UserDetail> users = new ArrayList<UserDetail>();
		users = session.createQuery("from UserDetail where referral=?")
				.setParameter(0, referral).list();
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public User findByUserName(String username, Session session)
			throws Exception {
		if (session == null)
			session = CommonUtil.getSession(sessionFactory);
		List<User> users = new ArrayList<User>();
		users = session.createQuery("from User where username=?")
				.setParameter(0, username).list();
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}

	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void updateUser(User user, Session session) throws Exception {
		session.merge(user);
		session.flush();
	}
}