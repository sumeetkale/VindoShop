package com.shopping.vindoshop.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.shopping.vindoshop.model.Bookmark;
import com.shopping.vindoshop.model.Outlet;
import com.shopping.vindoshop.model.User;

@Component
public class BookmarkDao {

	public boolean addBookmark(Bookmark bookmark, Session session)
			throws Exception {
		boolean bookmarkAdded = false;
		Serializable check = null;
		check = session.save(bookmark);
		session.flush();
		if (Integer.valueOf(check.toString()) > 0)
			bookmarkAdded = true;
		return bookmarkAdded;
	}

	public void deleteBookmark(int id, Session session) throws Exception {
		Bookmark bookmark = (Bookmark) session.load(Bookmark.class, id);
		session.delete(bookmark);
		session.flush();
	}

	@SuppressWarnings("unchecked")
	public Bookmark fetchBookmark(int id, Session session) throws Exception {
		List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		bookmarks = session.createQuery("from Bookmark where id=?")
				.setParameter(0, id).list();
		if (bookmarks.size() > 0) {
			return bookmarks.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Bookmark> fetchBookmarks(User user, Session session)
			throws Exception {
		List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		bookmarks = session.createQuery("from Bookmark where user=?")
				.setParameter(0, user).list();
		if (bookmarks.size() > 0) {
			return bookmarks;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Bookmark fetchBookmarkByOutlet(Outlet outlet, User user,
			Session session) throws Exception {
		List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		bookmarks = session
				.createQuery("from Bookmark where outlet=? and user =?")
				.setParameter(0, outlet).setParameter(1, user).list();
		if (bookmarks.size() > 0) {
			return bookmarks.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Bookmark> fetchTopBookmarks(User user, Session session)
			throws Exception {
		List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		bookmarks = session.createQuery("from Bookmark where user=?")
				.setParameter(0, user).setMaxResults(2).list();
		if (bookmarks.size() > 0) {
			return bookmarks;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean isBookmark(User user, Outlet outlet, Session session)
			throws Exception {
		List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		if (user == null)
			return false;
		bookmarks = session
				.createQuery("from Bookmark where user_id=? and outlet_id=?")
				.setParameter(0, user).setParameter(1, outlet).list();
		if (bookmarks.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void updateBookmark(Bookmark bookmark, Session session)
			throws Exception {
		session.merge(bookmark);
		session.flush();

	}

}