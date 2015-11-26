package com.shopping.vindoshop.controller.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopping.vindoshop.dao.BookmarkDao;
import com.shopping.vindoshop.dao.SocialDao;
import com.shopping.vindoshop.dao.UserDao;
import com.shopping.vindoshop.model.Result;
import com.shopping.vindoshop.model.User;
import com.shopping.vindoshop.model.UserDetail;
import com.shopping.vindoshop.util.CommonUtil;
import com.shopping.vindoshop.util.Constants;

@Controller
@RequestMapping("/api")
public class ApiUserController {
	@Autowired
	UserDao userDao;
	@Autowired
	BookmarkDao bookmarkDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private SocialDao socialDoa;

	/**
	 *
	 * @param request
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/checkUserExists", method = RequestMethod.GET)
	@ResponseBody
	public Result checkUserExists(HttpServletRequest request,
			@RequestParam String username) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			User user = userDao.findByUserName(username, session);
			if (user != null) {
				result.setMsg(Constants.UserAlreadyExists);
			} else {
				result.setMsg("");
			}
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	@RequestMapping(value = "/fetchUserDetail", method = RequestMethod.GET)
	public @ResponseBody Result fetchUserDetail(HttpServletRequest request) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (request.getUserPrincipal() != null) {
				User user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);
				if (user == null) {
					result.setMsg(Constants.InvalidCredentials);
				} else {
					UserDetail userDetails = userDao.fetchUserDetail(user,
							session);
					if (userDetails != null) {
						userDetails.setBookmarks(bookmarkDao.fetchTopBookmarks(
								user, session));
						userDetails.setCheckins(socialDoa
								.fetchAllCheckinHistoryForUser(session,
										user.getId()));
					} else
						userDetails = new UserDetail();
					result.setData(userDetails);
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

	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/uploadProfilePic")
	public Result fileUploaded(Model model, HttpServletRequest request,
			@RequestBody byte[] profileImage) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (request.getUserPrincipal() != null) {
				User user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);
				if (user == null) {
					result.setMsg(Constants.InvalidCredentials);
				} else {
					UserDetail userDetails = null;
					if (profileImage != null && profileImage.length > 0) {
						userDetails = userDao.fetchUserDetail(user, session);
						if (userDetails != null) {
							userDetails.setProfileImage(profileImage);
							userDao.addUserDetail(userDetails, session);
							userDetails.setBookmarks(bookmarkDao
									.fetchTopBookmarks(user, session));
							userDetails.setCheckins(socialDoa
									.fetchAllCheckinHistoryForUser(session,
											user.getId()));
						} else
							userDetails = new UserDetail();
					}
					result.setData(userDetails);
					result.setStatus(true);
					result.setMsg("Profile Image Uploaded");
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

	public BookmarkDao getBookmarkDao() {
		return bookmarkDao;
	}

	public Result getRegister(UserDetail userDetail, HttpServletRequest request) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		Transaction tx = session.getTransaction();
		UserDetail referralUser = null;
		if (userDetail.getUser() == null) {
			result.setMsg("Please enter user information");
		} else {
			try {
				tx.begin();
				userDetail.setOtp(CommonUtil.getRandom());
				if (userDetail.getUser().getPassword().equals("web"))
					userDetail.getUser().setPassword(
							Integer.toString(userDetail.getOtp()));
				userDetail.setPoints(100);
				if (userDetail.getReferral() != null
						&& !userDetail.getReferral().isEmpty()) {
					referralUser = userDao.findByReferral(
							userDetail.getReferral(), session);
					if (referralUser != null) {
						referralUser.setPoints(referralUser.getPoints() + 200);
						userDetail.setPoints(userDetail.getPoints() + 200);
					}
				}
				userDao.addUserDetail(userDetail, session);
				if (referralUser != null)
					userDao.addUserDetail(referralUser, session);
				CommonUtil.sendMsg(CommonUtil.getOTPMsgContent(
						userDetail.getFirstName(), userDetail.getOtp()), String
						.valueOf(userDetail.getPhone()));
				result.setData(userDetail);
				result.setMsg("User" + Constants.Added);
				result.setStatus(true);
				tx.commit();
			} catch (Exception e) {
				result.setMsg(CommonUtil.exceptionHandler(e));
				tx.rollback();
			} finally {
				session.close();
			}
		}
		return result;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public SocialDao getSocialDoa() {
		return socialDoa;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/leaderBoard")
	public @ResponseBody Result leaderBoard(HttpServletRequest request) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			User user = null;
			if (request.getUserPrincipal() != null)
				user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);

			result.setData(userDao.fetchleaderBoard(user, session));
			result.setStatus(true);
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public @ResponseBody Result login(HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (request.getUserPrincipal() != null) {
				User user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);
				if (user == null) {
					result.setMsg(Constants.InvalidCredentials);
				} else {
					UserDetail userDetails = userDao.fetchUserDetail(user,
							session);
					userDetails.setBookmarks(bookmarkDao.fetchBookmarks(user,
							session));
					userDetails.setCheckins(socialDoa
							.fetchAllCheckinHistoryForUser(session,
									user.getId()));
					result.setData(userDetails);
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

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody Result register(@RequestBody String userDetailStr,
			HttpServletRequest request) throws JsonParseException,
			JsonMappingException, IOException {
		org.codehaus.jackson.map.ObjectMapper objectMapper = new ObjectMapper();
		UserDetail userDetail = objectMapper.readValue(userDetailStr,
				UserDetail.class);
		return getRegister(userDetail, request);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/resetPwd")
	public @ResponseBody Result resetPwd(@RequestBody User user,
			HttpServletRequest request, @RequestParam String otp) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		User dbUser;
		try {
			if (otp.length() > 1) {
				if (request.getUserPrincipal() != null) {
					if (user.getUsername().equals(
							request.getUserPrincipal().getName())) {
						if (otp.length() != 4) {
							result.setMsg(Constants.OTPLength);
							return result;
						}
					} else {
						result.setMsg(Constants.InvalidCredentials);
						return result;
					}
				} else {
					result.setMsg(Constants.NoCredentials);
					return result;
				}
			}
			dbUser = userDao.findByUserName(user.getUsername(), session);
			if (dbUser == null) {
				result.setMsg(Constants.InvalidCredentials);
			} else {
				if (Integer.valueOf(otp) != 0) {
					UserDetail userDetail = userDao.fetchUserDetail(dbUser,
							session);
					if (Integer.valueOf(otp) != userDetail.getOtp()) {
						result.setMsg("OTP does not match");
						return result;
					}
				}
				if (user.getPassword() == null) {
					result.setMsg("Please Enter Password");
				} else {
					dbUser.setPassword(new BCryptPasswordEncoder().encode(user
							.getPassword()));
					userDao.updateUser(dbUser, session);
					result.setMsg("Password Reset Successfully!!!!");
					result.setStatus(true);
				}
			}
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/sendOtp")
	public @ResponseBody Result sendOtp(HttpServletRequest request,
			@RequestParam String phone, @RequestParam String userName) {
		Result result = new Result();
		if (phone.length() != 10) {
			result.setMsg(Constants.PhoneLength);
		} else {
			int otp = CommonUtil.getRandom();
			try {
				String response = CommonUtil.sendMsg(
						CommonUtil.getOTPMsgContent(userName, otp), phone);
				result.setMsg(response);
				result.setStatus(true);
			} catch (Exception e) {
				result.setMsg(CommonUtil.exceptionHandler(e));
			}
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/forgotPwd")
	public @ResponseBody Result sendPwd(@RequestBody User user,
			HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			user = userDao.findByUserName(user.getUsername(), session);
			if (user == null) {
				result.setMsg(Constants.InvalidCredentials);
			} else {
				UserDetail userDetail = userDao.fetchUserDetail(user, session);
				int otp = CommonUtil.getRandom();
				String responseStr = CommonUtil.sendMsg(
						CommonUtil.getOTPMsgContentForgot(
								userDetail.getFirstName(), otp),
						String.valueOf(userDetail.getPhone()));
				userDetail.setOtp(otp);
				if (userDetail.getUser().getPassword().equals("web"))
					userDetail.getUser().setPassword(
							Integer.toString(userDetail.getOtp()));
				userDao.addUserDetail(userDetail, session);
				result.setMsg(responseStr);
				result.setStatus(true);
			}
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	public void setBookmarkDao(BookmarkDao bookmarkDao) {
		this.bookmarkDao = bookmarkDao;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setSocialDoa(SocialDao socialDoa) {
		this.socialDoa = socialDoa;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@RequestMapping(value = "/updateUserDetail", method = RequestMethod.POST)
	public @ResponseBody Result updateUserDetailStr(
			@RequestBody String userDetailStr, HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException {
		org.codehaus.jackson.map.ObjectMapper objectMapper = new ObjectMapper();

		UserDetail userDetail = objectMapper.readValue(userDetailStr,
				UserDetail.class);

		return updateUserDetail(userDetail, request);
	}

	public Result updateUserDetail(UserDetail userDetail,
			HttpServletRequest request) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (request.getUserPrincipal() != null) {
				User user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);
				if (user == null) {
					result.setMsg(Constants.InvalidCredentials);
				} else {
					if (userDetail.getId() == 0) {
						UserDetail dbUserDetail = userDao.fetchUserDetail(user,
								session);
						if (dbUserDetail != null)
							userDetail.setId(dbUserDetail.getId());
					}
					userDetail.setUser(user);
					userDao.addUserDetail(userDetail, session);
					result.setMsg("User Detail" + Constants.Updated);
					result.setStatus(true);
					result.setData(userDetail);
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

	@RequestMapping(method = RequestMethod.GET, value = "/verifyOtp")
	public @ResponseBody Result verifyOtp(HttpServletRequest request,
			@RequestParam String otp, @RequestParam String userName) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		if (otp.length() != 4) {
			result.setMsg(Constants.OTPLength);
		} else {
			try {
				User user = userDao.findByUserName(userName, session);
				if (user == null) {
					result.setMsg(Constants.InvalidCredentials);
				} else {
					if (user.isActive())
						result.setMsg("User is already Active");
					else {
						UserDetail userDetails = userDao.fetchUserDetail(user,
								session);
						if (userDetails.getOtp() == Integer.valueOf(otp)) {
							user.setActive(true);
							userDao.updateUser(user, session);
							result.setMsg("OTP verified successfully");
							result.setStatus(true);
						} else
							result.setMsg("OTP does not match");
					}
				}
			} catch (Exception e) {
				result.setMsg(CommonUtil.exceptionHandler(e));
			} finally {
				session.close();
			}
		}
		return result;
	}
}
