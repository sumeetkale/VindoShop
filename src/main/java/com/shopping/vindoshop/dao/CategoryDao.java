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

import com.shopping.vindoshop.model.Business;
import com.shopping.vindoshop.model.Category;
import com.shopping.vindoshop.model.Result;

@Component
public class CategoryDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void addCategories(List<Category> categories, Session session)
			throws Exception {
		for (Iterator<Category> iterator = categories.iterator(); iterator
				.hasNext();) {
			Category category = iterator.next();
			session.saveOrUpdate(category);
		}
		session.flush();
	}

	public int addCategory(Category category, Session session) {
		int id = Integer.parseInt(session.save(category).toString());
		session.flush();
		return id;
	}

	public void deleteCategory(Category category, Session session) {
		session.delete(category);
		session.flush();
	}

	@SuppressWarnings("unchecked")
	public Result fetchAllCategories(int start, int range, Session session)
			throws HibernateException, ParseException {
		List<Category> categories = new ArrayList<Category>();
		Result result = new Result();
		categories = session.createQuery("from Category").list();
		if (categories.size() > 0) {
			if (categories.size() % range > 0)
				result.setLastPageNumber((categories.size() / range) + 1);
			else
				result.setLastPageNumber((categories.size() / range));
			if (start == 0)
				start = 1;
			if (categories.size() < start)
				result.setData(new ArrayList<Business>());
			else if (categories.size() < (start + range))
				result.setData(categories.subList(start - 1, categories.size()));
			else {
				result.setData(categories.subList(start - 1, (start + range)));
			}
			return result;
		} else {
			return new Result();
		}

	}

	@SuppressWarnings("unchecked")
	public Result fetchHomeCategories(int start, int range, Session session)
			throws HibernateException, ParseException {
		List<Category> categories = new ArrayList<Category>();
		Result result = new Result();
		categories = session.createQuery("from Category where name like '%s Wear'").list();
		if (categories.size() > 0) {
			if (categories.size() % range > 0)
				result.setLastPageNumber((categories.size() / range) + 1);
			else
				result.setLastPageNumber((categories.size() / range));
			if (start == 0)
				start = 1;
			if (categories.size() < start)
				result.setData(new ArrayList<Business>());
			else if (categories.size() < (start + range))
				result.setData(categories.subList(start - 1, categories.size()));
			else {
				result.setData(categories.subList(start - 1, (start + range)));
			}
			return result;
		} else {
			return new Result();
		}

	}
	@SuppressWarnings("unchecked")
	public Category fetchCategory(int storeId, Session session)
			throws HibernateException, ParseException {
		List<Category> categories = new ArrayList<Category>();
		categories = session.createQuery("from Category where id =?")
				.setParameter(0, storeId).list();
		if (categories.size() > 0) {
			return categories.get(0);
		} else {
			return null;
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

	public void updateCategory(Category category, Session session)
			throws HibernateException, ParseException {
		session.merge(category);
		session.flush();
	}
}