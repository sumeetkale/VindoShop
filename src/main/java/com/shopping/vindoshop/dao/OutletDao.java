package com.shopping.vindoshop.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopping.vindoshop.model.Category;
import com.shopping.vindoshop.model.Outlet;
import com.shopping.vindoshop.model.Result;

@Component
public class OutletDao {
	@Autowired
	private SessionFactory sessionFactory;

	public int addOutlet(Outlet outlet, Session session) {
		int id = Integer.parseInt(session.save(outlet).toString());
		session.flush();
		return id;
	}

	public void addOutlets(List<Outlet> outlets, Session session)
			throws Exception {
		for (Iterator<Outlet> iterator = outlets.iterator(); iterator.hasNext();) {
			Outlet outlet = iterator.next();
			session.saveOrUpdate(outlet);
		}
		session.flush();
	}

	public void deleteOutlet(Outlet outlet, Session session) {
		session.delete(outlet);
		session.flush();
	}

	@SuppressWarnings("unchecked")
	public Result fetchAllOutlets(int start, int range, Session session)
			throws HibernateException, ParseException {
		List<Outlet> outlets = new ArrayList<Outlet>();
		Result result = new Result();
		outlets = session.createQuery("from Outlet").setFirstResult(start)
				.setMaxResults(range).list();
		if (outlets.size() > 0) {
			if (outlets.size() % range > 0)
				result.setLastPageNumber((outlets.size() / range) + 1);
			else
				result.setLastPageNumber((outlets.size() / range));
			result.setData(outlets);
			return result;
		} else {
			return new Result();
		}

	}

	@SuppressWarnings("unchecked")
	public Category fetchCategoryByName(String categoryName, Session session)
			throws Exception {
		List<Category> categories = new ArrayList<Category>();
		categories = session.createQuery("from Category where name=?")
				.setParameter(0, categoryName).list();
		if (categories.size() > 0) {
			return categories.get(0);
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<String> fetchImages(int outletId, Session session)
			throws HibernateException, ParseException {
		List<String> images = new ArrayList<String>();
		images = session
				.createQuery("Select image from Images where outlet_id = ?")
				.setParameter(0, outletId).list();
		if (images.size() > 0) {
			return images;
		} else
			return null;

	}

	@SuppressWarnings("unchecked")
	public Outlet getOutletInfo(int storeId, Session session)
			throws HibernateException, ParseException {
		List<Outlet> outlets = new ArrayList<Outlet>();
		outlets = session.createQuery("from Outlet where id =?")
				.setParameter(0, storeId).list();
		if (outlets.size() > 0) {
			return outlets.get(0);
		} else {
			return null;
		}

	}

	public void updateOutlet(Outlet outlet, Session session)
			throws HibernateException, ParseException {
		session.merge(outlet);
		session.flush();
	}
}