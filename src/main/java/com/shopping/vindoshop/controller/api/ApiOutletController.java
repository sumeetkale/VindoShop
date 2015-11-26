package com.shopping.vindoshop.controller.api;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopping.vindoshop.dao.BookmarkDao;
import com.shopping.vindoshop.dao.CategoryDao;
import com.shopping.vindoshop.dao.OfferDao;
import com.shopping.vindoshop.dao.OutletDao;
import com.shopping.vindoshop.dao.UserDao;
import com.shopping.vindoshop.model.Outlet;
import com.shopping.vindoshop.model.Result;
import com.shopping.vindoshop.model.User;
import com.shopping.vindoshop.model.UserDetail;
import com.shopping.vindoshop.util.CommonUtil;
import com.shopping.vindoshop.util.Constants;

@Controller
@RequestMapping("/api")
public class ApiOutletController {

	@Autowired
	OutletDao outletDao;
	@Autowired
	OfferDao offerDao;

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private UserDao userDao;

	@Autowired
	CategoryDao categoryDao;

	@Autowired
	BookmarkDao bookmarkDao;

	@RequestMapping(value = "/addOutletRating", method = RequestMethod.POST)
	public @ResponseBody Result addOutletRating(HttpServletRequest request,
			@RequestBody Outlet outlet) {
		Result result = new Result();
		result.setData(outlet);
		Session session = CommonUtil.getSession(sessionFactory);
		User user;
		try {
			if (outlet.getId() == 0)
				result.setMsg(Constants.InvalidInput);
			else {
				if (request.getUserPrincipal() != null) {
					user = userDao.findByUserName(request.getUserPrincipal()
							.getName(), session);
					UserDetail userDetail = userDao.fetchUserDetail(user,
							session);
					if (userDetail == null) {
						result.setMsg("Please update your profile before giving rating!!!");
					} else {
						Outlet outletDB = outletDao.getOutletInfo(
								outlet.getId(), session);
						if (outlet.getNewRating() != null) {
							outletDB.setRatingCount(outletDB.getRatingCount() + 1);
							if (outlet.getNewRating().get(0) != 0)
								outletDB.setInventoryRating(((outletDB
										.getInventoryRating()
										* outletDB.getRatingCount() - 1) + outlet
										.getNewRating().get(0))
										/ outletDB.getRatingCount());
							if (outlet.getNewRating().get(1) != 0)
								outletDB.setStaffRating(((outletDB
										.getStaffRating()
										* outletDB.getRatingCount() - 1) + outlet
										.getNewRating().get(1))
										/ outletDB.getRatingCount());
							if (outlet.getNewRating().get(2) != 0)
								outletDB.setDiscountRating(((outletDB
										.getDiscountRating()
										* outletDB.getRatingCount() - 1) + outlet
										.getNewRating().get(2))
										/ outletDB.getRatingCount());
							if (outlet.getNewRating().get(3) != 0)
								outletDB.setRating(((outletDB.getRating()
										* outletDB.getRatingCount() - 1) + outlet
										.getNewRating().get(3))
										/ outletDB.getRatingCount());
						}
						outletDao.updateOutlet(outletDB, session);

						userDetail.setPoints(userDetail.getPoints() + 5);
						userDao.addUserDetail(userDetail, session);
						result.setStatus(true);
						result.setMsg("Thank you! for rating this store");
					}
				} else {
					result.setMsg(Constants.NoCredentials);
					return result;
				}
			}
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	@RequestMapping(value = "/fetchOutletCategories", method = RequestMethod.GET)
	public @ResponseBody Result fetchOutletCategories(
			HttpServletRequest request, @RequestParam int start,
			@RequestParam int range) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (start < 0 || range < 0)
				result.setMsg(Constants.InvalidInput);
			else {
				result = categoryDao.fetchHomeCategories(start, range, session);
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

	@RequestMapping(value = "/fetchOutletInfo", method = RequestMethod.GET)
	public @ResponseBody Result fetchOutletInfo(HttpServletRequest request,
			@RequestParam int outletId) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			Outlet outlet = outletDao.getOutletInfo(outletId, session);
			if (outlet == null)
				result.setMsg("Invalid Store");
			else {
				outlet.setImages(outletDao.fetchImages(outlet.getId(), session));
				outlet.setCurrentOffers(offerDao.fetchOfferForOutlet(
						outlet.getId(), session));

				outlet.setUpcomingOffers(offerDao.fetchUpcomingOfferForOutlet(
						outlet.getId(), session));

				if (request.getUserPrincipal() != null) {
					User user = userDao.findByUserName(request
							.getUserPrincipal().getName(), session);
					outlet.setOutletIsFav(bookmarkDao.isBookmark(user, outlet,
							session));
				}
				result.setData(outlet);
				result.setStatus(true);
			}
			if (result.getData() == null)
				result.setMsg(Constants.NoData);
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.clear();
		}
		return result;
	}

	public CategoryDao getCategoryDao() {
		return categoryDao;
	}

	public OfferDao getOfferDao() {
		return offerDao;
	}

	public OutletDao getOutletDao() {
		return outletDao;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public void setOfferDao(OfferDao offerDao) {
		this.offerDao = offerDao;
	}

	public void setOutletDao(OutletDao outletDao) {
		this.outletDao = outletDao;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
