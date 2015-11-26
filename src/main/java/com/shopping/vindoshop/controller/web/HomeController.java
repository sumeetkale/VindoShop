package com.shopping.vindoshop.controller.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.social.connect.Connection;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.shopping.vindoshop.controller.api.ApiBookmarkController;
import com.shopping.vindoshop.controller.api.ApiBusinessController;
import com.shopping.vindoshop.controller.api.ApiOfferController;
import com.shopping.vindoshop.controller.api.ApiSocialMediaController;
import com.shopping.vindoshop.controller.api.ApiUserController;
import com.shopping.vindoshop.dao.OfferDao;
import com.shopping.vindoshop.dao.UserDao;
import com.shopping.vindoshop.model.Business;
import com.shopping.vindoshop.model.Result;
import com.shopping.vindoshop.model.User;
import com.shopping.vindoshop.model.UserDetail;
import com.shopping.vindoshop.model.UserForm;
import com.shopping.vindoshop.util.CommonUtil;
import com.shopping.vindoshop.util.Constants;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@SuppressWarnings("restriction")
@Controller
public class HomeController {

	@Autowired
	OfferDao offerDao;
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	ApiOfferController offerController;

	@Autowired
	UserDao userDao;

	@Autowired
	ApiUserController userController;

	@Autowired
	ApiBookmarkController bookmarkController;

	@Autowired
	ApiBusinessController businessController;

	TwitterConnectionFactory connectionFactory;

	@Autowired
	ApiSocialMediaController social;

	// for 403 access denied page
	@Transactional
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied() {
		ModelAndView model = new ModelAndView();
		return model;

	}

	@RequestMapping(value = "businessSignup", method = RequestMethod.POST)
	public @ResponseBody ModelAndView businessSignup(
			HttpServletRequest request,
			@ModelAttribute("business") Business business) {
		ModelAndView model = new ModelAndView("home");
		try {

			Result result = businessController.addBusiness(request, business);
			model = loadHome(request, null);
			if (result.isStatus()) {
				model.addObject("msg", result.getMsg());
			} else
				model.addObject("error", result.getMsg());
		} catch (Exception ex) {
			model.addObject("error", CommonUtil.exceptionHandler(ex));
		}
		return model;
	}

	@RequestMapping("/callbackFBTwitter")
	public RedirectView callbackFBTwitter(HttpServletRequest request,
			@RequestParam String oauth_token,
			@RequestParam String oauth_verifier) {
		OAuthToken token = (OAuthToken) request.getSession().getAttribute(
				"requestToken");
		if (token.getValue().equals(oauth_token)) {
			AuthorizedRequestToken authorizedRequestToken = new AuthorizedRequestToken(
					token, oauth_verifier);

			OAuthToken accessToken = connectionFactory.getOAuthOperations()
					.exchangeForAccessToken(authorizedRequestToken, null);
			Connection<Twitter> connection = connectionFactory
					.createConnection(accessToken);
			social.socialRegister(connection.fetchUserProfile().getUsername()
					+ "@social.com", request, connection.fetchUserProfile()
					.getFirstName(), connection.fetchUserProfile()
					.getLastName(), connection.getImageUrl());
		}

		return new RedirectView(request.getContextPath() + "/");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changePwd")
	public ModelAndView changePwd(@ModelAttribute User user,
			HttpServletRequest request) {
		Result result = new Result();
		UserDetail userDetail = (UserDetail) userController.fetchUserDetail(
				request).getData();
		userDetail.getUser().setPassword(user.getPassword());
		result = userController.resetPwd(userDetail.getUser(), request,
				new String("0"));
		ModelAndView model = null;
		try {
			model = new ModelAndView("profile", "userDetail", userDetail);
			model.addObject("image",
					Base64.encode(userDetail.getProfileImage()));
			model.addObject("user", userDetail.getUser());
			if (result.isStatus())
				model.addObject("msg", result.getMsg());
			else
				model.addObject("error", result.getMsg());
		} catch (Exception e) {
			model.addObject("error", CommonUtil.exceptionHandler(e));
		}
		return model;
	}

	@RequestMapping(value = "/checkUserExists", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	public ArrayList<String> checkUserExists(HttpServletRequest request,
			@RequestParam String fieldValue) {

		ArrayList<String> arl = new ArrayList<String>();
		try {
			Result result = userController.checkUserExists(request, fieldValue);
			arl.add(request.getParameter("fieldId"));
			if (result.getMsg().isEmpty()) {
				arl.add("1");
			} else {
				arl.add("0");
			}

		} catch (Exception e) {
		}
		return arl;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/forgotPassword")
	public ModelAndView forgotPassword(HttpServletRequest request,
			HttpServletResponse response, @ModelAttribute User user) {
		ModelAndView model = null;
		Result result = new Result();
		try {
			result = userController.sendPwd(user, request, response);
			model = new ModelAndView();
			model.addObject("result", result);
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		}
		return model;
	}

	public ApiBookmarkController getBookmarkController() {
		return bookmarkController;
	}

	public ApiBusinessController getBusinessController() {
		return businessController;
	}

	// customize the error message
	private String getErrorMessage(HttpServletRequest request, String key) {
		Exception exception = (Exception) request.getSession()
				.getAttribute(key);
		String error = "";
		try {
			if (exception instanceof BadCredentialsException) {
				error = "Invalid username and password!";
			} else {
				error = exception.getMessage();
			}
		} catch (Exception e) {
			error = null;
		}
		return error;
	}

	public ApiUserController getUserController() {
		return userController;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/leaderBoard")
	public ModelAndView leaderBoard(HttpServletRequest request) {
		ModelAndView model = null;
		try {
			model = new ModelAndView("leaderBoard", "result",
					userController.leaderBoard(request));
		} catch (Exception e) {
			model.addObject("error", CommonUtil.exceptionHandler(e));
		}
		return model;
	}

	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public ModelAndView loadHome(HttpServletRequest request,
			@RequestParam(value = "error", required = false) String error) {
		ModelAndView m = new ModelAndView("home");
		Result result = new Result();
		Map<String, Result> model = new HashMap<String, Result>();
		try {
			Result tending_result = offerController.fetchTrendingOffer(request,
					Constants.START, Constants.RANGE, "relevance", "all");
			model.put("trending_offers", tending_result);
			Result mall_result = offerController.fetchMallOffer(request,
					Constants.START, Constants.RANGE, "relevance", "all",false);
			model.put("mall_offers", mall_result);
			Result exclusive_result = offerController.fetchExclusiveOffer(
					request, Constants.START, Constants.RANGE, "relevance",
					"all",false);
			model.put("exclusive_offers", exclusive_result);
			result.setData(model);
			result.setStatus(true);
			result.setMsg("Offers" + Constants.Added);

			if (error != null) {
				String temp = getErrorMessage(request,
						"SPRING_SECURITY_LAST_EXCEPTION");
				if (temp == null)
					m.addObject("error", "Invalid Credentials !!!!!");
				else if (temp.equals(""))
					m.addObject("error", error);
				else
					m.addObject("error", temp);
			}
		} catch (Exception e) {
			m.addObject("error", CommonUtil.exceptionHandler(e));
		}
		m.addObject("result", result);
		return m;
	}

	@RequestMapping("/logoutTwitter")
	public void logoutTwitter(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		response.sendRedirect(request.getContextPath() + "/");

	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/register")
	public ModelAndView processRegistration(
			@ModelAttribute("userForm") UserForm userForm,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		UserDetail userDetail = new UserDetail();
		userDetail.setFirstName(userForm.getFirstName());
		userDetail.setLastName(userForm.getLastName());
		userDetail.setCity(userForm.getCity());
		userDetail.setPinCode(userForm.getPinCode());
		userDetail.setReferral(userForm.getReferral());
		userDetail.setDateofBirth(userForm.getDateOfBirth());
		userDetail.setSubscribe(userForm.isSubscribe());
		userDetail.setPhone(userForm.getPhone());
		userDetail.setUser(new User());
		userDetail.getUser().setUsername(userForm.getUsername());
		userDetail.getUser().setPassword("web");
		userDetail.getUser().setActive(true);
		Result result = userController.getRegister(userDetail, request);
		model = loadHome(request, result.getMsg());
		if (result.isStatus()) {
			model.addObject("error", null);
			model.addObject("msg",
					"Registration Successful, Please login using OTP sent on your mobile");
		}
		return model;
	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/profile")
	public ModelAndView processUserDetail(
			@ModelAttribute("userDetail") UserDetail userDetail,
			HttpServletRequest request) {
		Result result = new Result();
		userDetail.setProfileImage(((UserDetail) result.getData())
				.getProfileImage());
		userController.updateUserDetail(userDetail, request);
		ModelAndView model = null;
		try {
			result = userController.fetchUserDetail(request);
			model = new ModelAndView("profile", "userDetail", result.getData());
			model.addObject("userDetail", result.getData());
			model.addObject("user", ((UserDetail) result.getData()).getUser());
			if (result.isStatus())
				model.addObject("msg", result.getMsg());
			else
				model.addObject("error", result.getMsg());
		} catch (Exception e) {
			model.addObject("error", CommonUtil.exceptionHandler(e));
		}
		return model;
	}

	@RequestMapping(value = { "/profile", "/updateProfile", "changePwd" }, method = RequestMethod.GET)
	public ModelAndView profile(HttpServletRequest request) {

		Result result = new Result();
		ModelAndView model = null;
		try {
			result = userController.fetchUserDetail(request);
			UserDetail userDetail = (UserDetail) result.getData();
			model = new ModelAndView("profile");
			model.addObject("userDetail", userDetail);
			model.addObject("image",
					Base64.encode(userDetail.getProfileImage()));
			model.addObject("user", userDetail.getUser());
			if (result.isStatus()) {
				model.addObject("msg", result.getMsg());
			} else
				model.addObject("error", result.getMsg());
		} catch (Exception e) {
			model.addObject("error", CommonUtil.exceptionHandler(e));
		}
		return model;
	}

	public void setBookmarkController(ApiBookmarkController bookmarkController) {
		this.bookmarkController = bookmarkController;
	}

	public void setBusinessController(ApiBusinessController businessController) {
		this.businessController = businessController;
	}

	public void setUserController(ApiUserController userController) {
		this.userController = userController;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@RequestMapping(value = "/twitterlogin", method = RequestMethod.GET)
	public RedirectView twitterView(HttpServletRequest request) {
		connectionFactory = new TwitterConnectionFactory(
				"m4m3uT2NT3phiZsDfhCz7w6iO",
				"nvaky87895eOhRB6QG50pgmRooaB6sVupAM6KBI7Hye6sLdSjp");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("x_auth_access_type", "read");
		StringBuffer callbackURL = request.getRequestURL();
		int index = callbackURL.lastIndexOf("/");
		callbackURL.replace(index, callbackURL.length(), "").append(
				"/callbackFBTwitter");
		OAuthToken token = connectionFactory.getOAuthOperations()
				.fetchRequestToken(callbackURL.toString(), params);
		request.getSession().setAttribute("requestToken", token);
		String url = connectionFactory.getOAuthOperations()
				.buildAuthenticateUrl(token.getValue(), OAuth1Parameters.NONE);

		return new RedirectView(url);
	}

	@RequestMapping(value = { "/updateProfile" }, method = RequestMethod.POST)
	public ModelAndView updateProfile(HttpServletRequest request, Model model1,
			@ModelAttribute UserDetail userDetail) {

		Result result = new Result();
		ModelAndView model = null;
		try {
			UserDetail userDetaildb = (UserDetail) userController
					.fetchUserDetail(request).getData();
			userDetail.setPoints(userDetaildb.getPoints());
			userDetail.setReferral(userDetaildb.getReferral());
			result = userController.updateUserDetail(userDetail, request);
			model = new ModelAndView("profile");
			userDetail = (UserDetail) result.getData();
			model.addObject("userDetail", userDetail);
			if (userDetail.getProfileImage() != null)
				model.addObject("image",
						Base64.encode(userDetail.getProfileImage()));
			model.addObject("user", userDetail.getUser());
			if (result.isStatus()) {
				model.addObject("msg", result.getMsg());
			} else
				model.addObject("error", result.getMsg());
		} catch (Exception e) {
			model.addObject("error", CommonUtil.exceptionHandler(e));
		}
		return model;
	}

	@RequestMapping(value = { "/uploadImage" }, method = RequestMethod.POST)
	public ModelAndView uploadImage(HttpServletRequest request, Model model1,
			@ModelAttribute UserDetail userDetail) {

		Result result = new Result();
		ModelAndView model = null;
		try {
			result = userController.fileUploaded(model1, request,
					userDetail.getProfileImage());
			model = new ModelAndView("profile");
			userDetail = (UserDetail) result.getData();
			model.addObject("userDetail", userDetail);
			model.addObject("image",
					Base64.encode(userDetail.getProfileImage()));
			model.addObject("user", userDetail.getUser());
			if (result.isStatus()) {
				model.addObject("msg", result.getMsg());
			} else
				model.addObject("error", result.getMsg());
		} catch (Exception e) {
			model.addObject("error", CommonUtil.exceptionHandler(e));
		}
		return model;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/register")
	public ModelAndView viewRegistration(HttpServletRequest request) {
		ModelAndView model = null;
		try {
			UserForm userDetail = new UserForm();
			model = new ModelAndView();
			model.addObject("userForm", userDetail);
		} catch (Exception e) {
			model.addObject("error", CommonUtil.exceptionHandler(e));
		}
		return model;
	}

	@RequestMapping(value = "/wishlist", method = RequestMethod.GET)
	public ModelAndView wishlist(HttpServletRequest request) {

		ModelAndView model = null;
		try {
			model = new ModelAndView("wishlist", "result",
					bookmarkController.fetchBookmarks(request));
		} catch (Exception e) {
			model.addObject("error", CommonUtil.exceptionHandler(e));
		}
		return model;
	}

	@Transactional
	@RequestMapping(value = "/termsAndConditions")
	public ModelAndView termsAndConditions() {
		ModelAndView model = new ModelAndView();
		model.setViewName("termsAndConditions");
		return model;
	}

	@RequestMapping(value = "/privacyPolicy")
	public ModelAndView privacyPolicy() {
		ModelAndView model = new ModelAndView();
		model.setViewName("privacyPolicy");
		return model;
	}

}
