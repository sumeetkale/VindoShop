package com.shopping.vindoshop.controller.api;

import java.util.List;

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
import com.shopping.vindoshop.dao.OutletDao;
import com.shopping.vindoshop.dao.UserDao;
import com.shopping.vindoshop.model.Bookmark;
import com.shopping.vindoshop.model.Outlet;
import com.shopping.vindoshop.model.Result;
import com.shopping.vindoshop.model.User;
import com.shopping.vindoshop.util.CommonUtil;
import com.shopping.vindoshop.util.Constants;

@Controller
@RequestMapping("/api")
public class ApiBookmarkController {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	BookmarkDao bookmarkDao;
	@Autowired
	UserDao userDao;

	@RequestMapping(value = "/addBookmark", method = RequestMethod.POST)
	public @ResponseBody Result addBookmark(HttpServletRequest request,
			@RequestBody Bookmark bookmark) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (request.getUserPrincipal() != null) {
				User user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);
				if (user == null) {
					result.setMsg(Constants.InvalidCredentials);
				} else if (bookmark.getOutlet() == null)
					result.setMsg(Constants.InvalidInput);
				else {
					bookmark.setUser(user);
					if (bookmarkDao.addBookmark(bookmark, session)) {
						result.setStatus(true);
						result.setMsg("Store Added to WishList");
					}
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

	@RequestMapping(value = "/addBookmark", method = RequestMethod.GET)
	public @ResponseBody Result addBookmark(HttpServletRequest request,
			@RequestParam int outletId) {
		Bookmark bookmark = new Bookmark();
		Outlet outlet = new Outlet();
		outlet.setId(outletId);
		bookmark.setOutlet(outlet);
		return addBookmark(request, bookmark);
	}

	@RequestMapping(value = "/fetchBookmarks", method = RequestMethod.GET)
	public @ResponseBody Result fetchBookmarks(HttpServletRequest request) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (request.getUserPrincipal() != null) {
				User user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);
				if (user == null) {
					result.setMsg(Constants.InvalidCredentials);
				} else {
					List<Bookmark> bookmarks = bookmarkDao.fetchBookmarks(user,
							session);
					result.setData(bookmarks);
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

	public BookmarkDao getBookmarkDao() {
		return bookmarkDao;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	@RequestMapping(value = "/removeBookmark", method = RequestMethod.GET)
	public @ResponseBody Result removeBookmark(HttpServletRequest request,
			@RequestParam int bookmarkId) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (request.getUserPrincipal() != null) {
				User user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);
				if (user == null) {
					result.setMsg(Constants.InvalidCredentials);
				} else {
					Bookmark bookmark = bookmarkDao.fetchBookmark(bookmarkId,
							session);
					if (bookmark == null || bookmark.getUser() != user)
						result.setMsg(Constants.NoData);
					else {
						bookmarkDao.deleteBookmark(bookmarkId, session);
						result.setMsg("Bookmark" + Constants.Deleted);
						result.setStatus(true);
					}
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

	@Autowired
	OutletDao outletDao;

	@RequestMapping(value = "/removeOutletBookmark", method = RequestMethod.GET)
	public @ResponseBody Result removeOutletBookmark(
			HttpServletRequest request, @RequestParam int outletId) {
		Result result = new Result();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			if (request.getUserPrincipal() != null) {
				User user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);
				if (user == null) {
					result.setMsg(Constants.InvalidCredentials);
				} else {
					Bookmark bookmark = bookmarkDao.fetchBookmarkByOutlet(
							outletDao.getOutletInfo(outletId, session), user,
							session);
					if (bookmark == null || bookmark.getUser() != user)
						result.setMsg(Constants.NoData);
					else {
						bookmarkDao.deleteBookmark(bookmark.getId(), session);
						result.setMsg("Bookmark Removed!!");
						result.setStatus(true);
					}
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

	public void setBookmarkDao(BookmarkDao bookmarkDao) {
		this.bookmarkDao = bookmarkDao;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
