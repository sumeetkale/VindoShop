package com.shopping.vindoshop.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.shopping.vindoshop.model.CheckinHistory;
import com.shopping.vindoshop.model.Outlet;
import com.shopping.vindoshop.model.Result;
import com.shopping.vindoshop.util.CommonUtil;

@Component
public class SocialDao {

	public boolean addCheckin(CheckinHistory checkin, Session session)
			throws Exception {
		boolean checkinAdded = false;
		Serializable check = null;
		check = session.save(checkin);
		session.flush();
		if (Integer.valueOf(check.toString()) > 0)
			checkinAdded = true;
		return checkinAdded;
	}

	@SuppressWarnings("unchecked")
	public Result fetchAllCheckinHistoryForStore(Session session,
			Outlet outletId, int start, int range) throws Exception {
		List<CheckinHistory> checkin = new ArrayList<CheckinHistory>();
		Result result = new Result();
		checkin = session.createQuery("from CheckinHistory where outlet_id=?")
				.setParameter(0, outletId).setMaxResults(range)
				.setFirstResult(start).list();
		if (checkin.size() > 0) {
			if (checkin.size() % range > 0)
				result.setLastPageNumber((checkin.size() / range) + 1);
			else
				result.setLastPageNumber((checkin.size() / range));
			result.setData(checkin);
			return result;
		} else {
			return new Result();
		}
	}

	@SuppressWarnings("unchecked")
	public List<CheckinHistory> fetchAllCheckinHistoryForUser(Session session,
			int userId) throws Exception {
		List<CheckinHistory> checkin = new ArrayList<CheckinHistory>();
		checkin = session
				.createQuery(
						"from CheckinHistory where user_id=? ORDER BY checkinDate ASC")
				.setParameter(0, userId).setMaxResults(3).list();
		if (checkin.size() > 0) {
			return checkin;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Result fetchAllCheckins(Session session, int start, int range)
			throws Exception {
		List<CheckinHistory> checkin = new ArrayList<CheckinHistory>();
		Result result = new Result();
		checkin = session.createQuery("from CheckinHistory ").list();
		if (checkin.size() > 0) {
			if (checkin.size() % range > 0)
				result.setLastPageNumber((checkin.size() / range) + 1);
			else
				result.setLastPageNumber((checkin.size() / range));
			if (start == 0)
				start = 1;
			if (checkin.size() < start)
				result.setData(new ArrayList<CheckinHistory>());
			else if (checkin.size() < (start + range))
				result.setData(checkin.subList(start - 1, checkin.size()));
			else
				result.setData(checkin.subList(start - 1, (start + range)));
			return result;
		} else {
			return new Result();
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public boolean hasCheckedInToday(CheckinHistory checkin, Session session)
			throws Exception {
		boolean checkedin = false;
		Date startDate = CommonUtil.getIstDateTime();
		Date endDate = CommonUtil.getIstDateTime();

		startDate.setHours(0);
		startDate.setMinutes(0);
		startDate.setSeconds(0);

		endDate.setHours(23);
		endDate.setMinutes(59);
		endDate.setSeconds(59);
		List<CheckinHistory> dbCheckin = session
				.createQuery(
						"from CheckinHistory where outlet_id=? and user_id=? and checkinDate >=? and checkinDate <=?")
				.setParameter(0, checkin.getOutlet())
				.setParameter(1, checkin.getUser()).setParameter(2, startDate)
				.setParameter(3, endDate).list();
		if (dbCheckin.size() > 0) {
			checkedin = true;
		}
		return checkedin;
	}

}