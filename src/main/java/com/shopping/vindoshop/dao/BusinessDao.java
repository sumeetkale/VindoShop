package com.shopping.vindoshop.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.shopping.vindoshop.model.Business;
import com.shopping.vindoshop.model.Result;

@Component
public class BusinessDao {

	public boolean addBusiness(Business business, Session session)
			throws Exception {
		boolean businessAdded = false;
		Serializable check = null;
		check = session.save(business);
		session.flush();
		if (Integer.valueOf(check.toString()) > 0)
			businessAdded = true;
		return businessAdded;
	}

	public void deleteBusiness(Business business, Session session) {
		session.delete(business);
		session.flush();
	}

	@SuppressWarnings("unchecked")
	public Result fetchAllBusiness(Session session, int range, int start)
			throws Exception {
		List<Business> business = new ArrayList<Business>();
		Result result = new Result();
		business = session.createQuery("from Business").list();
		if (business.size() > 0) {
			if (business.size() % range > 0)
				result.setLastPageNumber((business.size() / range) + 1);
			else
				result.setLastPageNumber((business.size() / range));
			if (start == 0)
				start = 1;
			if (business.size() < start)
				result.setData(new ArrayList<Business>());
			else if (business.size() < (start + range))
				result.setData(business.subList(start - 1, business.size()));
			else
				result.setData(business.subList(start - 1, (start + range)));
			return result;
		} else {
			return new Result();
		}
	}

	@SuppressWarnings("unchecked")
	public Business fetchBusiness(int id, Session session) throws Exception {
		List<Business> business = new ArrayList<Business>();
		business = session.createQuery("from Business where id=?")
				.setParameter(0, id).list();
		if (business.size() > 0) {
			return business.get(0);
		} else {
			return null;
		}
	}

	public void updateBusiness(Business business, Session session)
			throws Exception {
		session.merge(business);
		session.flush();

	}

}