package com.shopping.vindoshop.controller.api;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopping.vindoshop.dao.BookmarkDao;
import com.shopping.vindoshop.dao.OfferDao;
import com.shopping.vindoshop.dao.UserDao;
import com.shopping.vindoshop.model.Offer;
import com.shopping.vindoshop.model.Result;
import com.shopping.vindoshop.model.SearchResult;
import com.shopping.vindoshop.model.User;
import com.shopping.vindoshop.util.CommonUtil;
import com.shopping.vindoshop.util.Constants;

@Controller
@RequestMapping("/api")
public class ApiOfferController {

	@Autowired
	OfferDao offerDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	UserDao userDao;

	@Autowired
	BookmarkDao bookmarkDao;

	@RequestMapping(value = "/fetchExclusiveOffer", method = RequestMethod.GET)
	public @ResponseBody SearchResult fetchExclusiveOffer(
			HttpServletRequest request, @RequestParam int start,
			@RequestParam int range,@RequestParam String sort,@RequestParam String category,@RequestParam boolean reverse) {
		SearchResult result = new SearchResult();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (start < 0 || range < 0)
				result.setMsg(Constants.InvalidInput);
			else {
				User user = null;
				if (request.getUserPrincipal() != null)
					user = userDao.findByUserName(request.getUserPrincipal()
							.getName(), session);
				result = offerDao.getExclusiveOffer(start, range, session,
						sort, category, user,reverse);
				result.setStatus(true);
				if (result.getData() == null)
					result.setMsg(Constants.NoData);
			}
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	@RequestMapping(value = "/fetchMallOffer", method = RequestMethod.GET)
	public @ResponseBody SearchResult fetchMallOffer(
			HttpServletRequest request, @RequestParam int start,
			@RequestParam int range, @RequestParam String sort,@RequestParam String category,@RequestParam boolean reverse) {
		SearchResult result = new SearchResult();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (start < 0 || range < 0)
				result.setMsg(Constants.InvalidInput);
			else {
				User user = null;
				if (request.getUserPrincipal() != null)
					user = userDao.findByUserName(request.getUserPrincipal()
							.getName(), session);
				result = offerDao.getMallOffer(start, range, session, sort,
						category, user,reverse);
				result.setStatus(true);
				if (result.getData() == null)
					result.setMsg(Constants.NoData);
			}
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	@RequestMapping(value = "/fetchOffer", method = RequestMethod.GET)
	public @ResponseBody Result fetchOffer(HttpServletRequest request,
			int offerId) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			User user = null;
			result.setData(offerDao.fetchOffer(offerId, session));
			if (request.getUserPrincipal() != null) {
				user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);
				((Offer) result.getData()).getOutlet()
						.setOutletIsFav(
								bookmarkDao.isBookmark(user,
										((Offer) result.getData()).getOutlet(),
										session));
			}
			result.setStatus(true);
			if (result.getData() == null)
				result.setMsg(Constants.NoData);
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	@RequestMapping(value = "/fetchOfferByCategory", method = RequestMethod.GET)
	public @ResponseBody SearchResult fetchOfferByCategory(
			HttpServletRequest request, @RequestParam int start,
			@RequestParam int range, @RequestParam String sort,
			@RequestParam String category, @RequestParam boolean reverse) {
		SearchResult result = new SearchResult();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (start < 0 || range < 0)
				result.setMsg(Constants.InvalidInput);
			else {
				User user = null;
				if (request.getUserPrincipal() != null)
					user = userDao.findByUserName(request.getUserPrincipal()
							.getName(), session);
				result = offerDao.getActiveOffers(start, range, session, sort,
						category, user,reverse);
				result.setStatus(true);
				if (result.getData() == null)
					result.setMsg(Constants.NoData);
			}
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	@RequestMapping(value = "/fetchTrendingOffer", method = RequestMethod.GET)
	public @ResponseBody SearchResult fetchTrendingOffer(
			HttpServletRequest request, @RequestParam int start,
			@RequestParam int range, String sort, String category) {
		SearchResult result = new SearchResult();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (start < 0 || range < 0)
				result.setMsg(Constants.InvalidInput);
			else {
				User user = null;
				if (request.getUserPrincipal() != null)
					user = userDao.findByUserName(request.getUserPrincipal()
							.getName(), session);
				result = offerDao.getTrendingOffer(start, range, session, sort,
						category, user);
				result.setStatus(true);
				if (result.getData() == null)
					result.setMsg(Constants.NoData);
			}
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	public OfferDao getOfferDao() {
		return offerDao;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setOfferDao(OfferDao offerDao) {
		this.offerDao = offerDao;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
