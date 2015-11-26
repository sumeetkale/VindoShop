package com.shopping.vindoshop.controller.api;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopping.vindoshop.dao.BusinessDao;
import com.shopping.vindoshop.dao.OutletDao;
import com.shopping.vindoshop.dao.UserDao;
import com.shopping.vindoshop.model.Business;
import com.shopping.vindoshop.model.Result;
import com.shopping.vindoshop.util.CommonUtil;
import com.shopping.vindoshop.util.Constants;

@Controller
@RequestMapping("/api")
public class ApiBusinessController {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	BusinessDao businessDao;
	@Autowired
	UserDao userDao;
	@Autowired
	OutletDao outletDao;

	@RequestMapping(value = "/addBusiness", method = RequestMethod.POST)
	public @ResponseBody Result addBusiness(HttpServletRequest request,
			@RequestBody Business business) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (businessDao.addBusiness(business, session)) {
				result.setStatus(true);
				result.setMsg("Business" + Constants.Added);
			}
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	public BusinessDao getBusinessDao() {
		return businessDao;
	}

	public OutletDao getOutletDao() {
		return outletDao;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setBusinessDao(BusinessDao businessDao) {
		this.businessDao = businessDao;
	}

	public void setOutletDao(OutletDao outletDao) {
		this.outletDao = outletDao;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
