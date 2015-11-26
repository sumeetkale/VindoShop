package com.shopping.vindoshop.controller.api;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopping.vindoshop.dao.OutletDao;
import com.shopping.vindoshop.dao.SocialDao;
import com.shopping.vindoshop.dao.UserDao;
import com.shopping.vindoshop.model.CheckinHistory;
import com.shopping.vindoshop.model.Result;
import com.shopping.vindoshop.model.User;
import com.shopping.vindoshop.model.UserDetail;
import com.shopping.vindoshop.service.UserService;
import com.shopping.vindoshop.util.CommonUtil;
import com.shopping.vindoshop.util.Constants;

@Controller
@RequestMapping("/api")
public class ApiSocialMediaController {

	@Autowired
	UserDao userDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private SocialDao socialDao;

	@Autowired
	OutletDao outletDoa;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	UserService userService;

	/**
	 * Authenticate the user
	 *
	 * @param user
	 * @param request
	 */
	private void authenticateUserAndSetSession(User user,
			HttpServletRequest request) {
		// generate session if one doesn't exist
		HttpSession session = request.getSession();
		// Authenticate the user
		UserDetails userDetails = userService.loadUserByUsername(user
				.getUsername());
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				userDetails.getUsername(), "Social",
				userDetails.getAuthorities());
		Authentication authentication = authenticationManager
				.authenticate(auth);
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}

	@RequestMapping(value = "checkIn", method = RequestMethod.POST)
	public @ResponseBody Result checkIn(HttpServletRequest request,
			@RequestBody CheckinHistory checkin) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (request.getUserPrincipal() != null) {
				User user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);
				if (user == null) {
					result.setMsg(Constants.InvalidCredentials);
				} else if (socialDao.hasCheckedInToday(checkin, session)) {
					result.setMsg("You Have Already Checked In today");
					result.setStatus(false);
				} else {
					checkin.setCheckinDate(CommonUtil.getIstDateTime());
					checkin.setUser(user);
					socialDao.addCheckin(checkin, session);
					UserDetail userdetail = userDao.fetchUserDetail(
							checkin.getUser(), session);
					userdetail.setPoints(userdetail.getPoints() + 5);
					userDao.addUserDetail(userdetail, session);
					result.setMsg("Checked In Successfully");
					result.setStatus(true);
				}
			} else
				result.setMsg(Constants.NoCredentials);
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	public OutletDao getOutletDoa() {
		return outletDoa;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public SocialDao getSocialDao() {
		return socialDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setOutletDoa(OutletDao outletDoa) {
		this.outletDoa = outletDoa;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setSocialDao(SocialDao socialDao) {
		this.socialDao = socialDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@RequestMapping(value = "/shareLike", method = RequestMethod.GET)
	public @ResponseBody Result share(HttpServletRequest request,
			@RequestParam String shareLike) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (request.getUserPrincipal() != null) {
				User user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);
				if (user == null) {
					result.setMsg(Constants.InvalidCredentials);
				} else {
					UserDetail userdetail = userDao.fetchUserDetail(userDao
							.findByUserName(request.getUserPrincipal()
									.getName(), session), session);
					if (shareLike.equalsIgnoreCase("Like"))
						userdetail.setPoints(userdetail.getPoints() + 50);
					else
						userdetail.setPoints(userdetail.getPoints() + 5);
					userDao.addUserDetail(userdetail, session);
					result.setStatus(true);
					result.setMsg(shareLike + " Successfull!!!");
				}
			} else
				result.setMsg(Constants.NoCredentials);
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	@RequestMapping(value = "/socialRegister", method = RequestMethod.GET)
	public @ResponseBody Result socialRegister(@RequestParam String userName,
			HttpServletRequest request, @RequestParam String firstName,
			@RequestParam String lastName, @RequestParam String imageUrl) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		Transaction tx = session.getTransaction();
		try {
			tx.begin();
			User user = userDao.findByUserName(userName, session);
			UserDetail userDetail;
			if (user == null) {
				user = new User();
				user.setActive(true);
				user.setPassword("Social");
				user.setUsername(userName);
				userDetail = new UserDetail();
				userDetail.setPoints(100);
				userDetail.setDateofBirth(new Date());
				userDetail.setUser(user);
			} else
				userDetail = userDao.fetchUserDetail(user, session);
			userDetail.setProfileImage(CommonUtil.extractBytes(imageUrl));
			userDetail.setFirstName(firstName);
			userDetail.setLastName(lastName);
			userDao.addUserDetail(userDetail, session);
			result.setStatus(true);
			result.setMsg("User logged in");
			tx.commit();
			authenticateUserAndSetSession(user, request);
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
			tx.rollback();
		} finally {
			session.close();
		}
		return result;
	}
}