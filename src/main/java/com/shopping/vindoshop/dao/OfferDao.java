package com.shopping.vindoshop.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopping.vindoshop.model.Bookmark;
import com.shopping.vindoshop.model.Category;
import com.shopping.vindoshop.model.Offer;
import com.shopping.vindoshop.model.Outlet;
import com.shopping.vindoshop.model.SearchResult;
import com.shopping.vindoshop.model.User;
import com.shopping.vindoshop.util.CommonUtil;

@Component
public class OfferDao {
	@Autowired
	CategoryDao categoryDao;

	@Autowired
	BookmarkDao bookmarkDao;

	public int addOffer(Offer offer, Session session) {
		int id = (int) session.save(offer);
		session.flush();
		return id;
	}

	public void addOffers(List<Offer> offers, Session session) throws Exception {
		for (Iterator<Offer> iterator = offers.iterator(); iterator.hasNext();) {
			Offer offer = iterator.next();
			session.saveOrUpdate(offer);
		}
		session.flush();

	}

	public void deleteOffer(Offer offer, Session session) {
		session.delete(offer);
		session.flush();
	}

	@SuppressWarnings("unchecked")
	public Offer fetchOffer(int id, Session session) throws Exception {
		List<Offer> offers = new ArrayList<Offer>();
		offers = session.createQuery("from Offer where id=?")
				.setParameter(0, id).list();
		if (offers.size() > 0) {
			return offers.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Offer> fetchOfferForOutlet(int outletId, Session session)
			throws Exception {
		List<Offer> offers = new ArrayList<Offer>();
		Date startDate = CommonUtil.getIstDateTime();
		Date endDate = CommonUtil.getIstDateTime();

		startDate.setHours(0);
		startDate.setMinutes(0);
		startDate.setSeconds(0);

		endDate.setHours(23);
		endDate.setMinutes(59);
		endDate.setSeconds(59);
		offers = session
				.createQuery(
						"Select id as id, image as image, startDate as startDate, endDate as endDate, detail as detail, shortDesc as shortDesc from Offer where outlet_id=? and startDate <= ? and endDate >= ?")
				.setParameter(0, outletId).setParameter(1, startDate)
				.setParameter(2, endDate)
				.setResultTransformer(Transformers.aliasToBean(Offer.class))
				.list();
		if (offers.size() > 0) {
			return offers;
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Offer> fetchUpcomingOfferForOutlet(int outletId, Session session)
			throws Exception {
		List<Offer> offers = new ArrayList<Offer>();
		Date startDate = CommonUtil.getIstDateTime();
		Date endDate = CommonUtil.getIstDateTime();

		startDate.setHours(0);
		startDate.setMinutes(0);
		startDate.setSeconds(0);

		endDate.setHours(23);
		endDate.setMinutes(59);
		endDate.setSeconds(59);
		endDate.setDate(endDate.getDate() + 5);

		offers = session
				.createQuery(
						"Select id as id, image as image, startDate as startDate, endDate as endDate, detail as detail, shortDesc as shortDesc from Offer where outlet_id=? and startDate > ? and endDate <= ?")
				.setParameter(0, outletId).setParameter(1, startDate)
				.setParameter(2, endDate)
				.setResultTransformer(Transformers.aliasToBean(Offer.class))
				.list();
		if (offers.size() > 0) {
			return offers;
		} else {
			return null;
		}
	}

	public SearchResult getActiveOffers(int start, int range, Session session,
			String sort, String category, User user,boolean reverse) throws Exception {
		return paginationOfferQueryExecutor(
				" from offer o join outlet outlt join category cOuter join outletcategory oc "
						+ "Where oc.outlet_id=outlt.id and cOuter.id=oc.category_id and  o.startDate <= ? and o.endDate >= ? and o.outlet_id=outlt.id",
				start, range, session, sort, category, user,reverse);
	}

	public CategoryDao getCategoryDao() {
		return categoryDao;
	}

	public SearchResult getExclusiveOffer(int start, int range,
			Session session, String sort, String category, User user,boolean reverse)
			throws Exception {
		return paginationOfferQueryExecutor(
				"from offer o join outlet outlt join category cOuter join outletcategory oc "
						+ "Where oc.outlet_id=outlt.id and cOuter.id=oc.category_id and  o.exclusive = true and o.startDate <= ? and o.endDate >= ? and o.outlet_id=outlt.id",
				start, range, session, sort, category, user,reverse);
	}

	public SearchResult getMallOffer(int start, int range, Session session,
			String sort, String category, User user,boolean reverse) throws Exception {
		return paginationOfferQueryExecutor(
				"from offer o join outlet outlt join category cOuter join outletcategory oc "
						+ "Where oc.outlet_id=outlt.id and cOuter.id=oc.category_id and o.startDate <= ? and o.endDate >= ? and o.outlet_id=outlt.id and outlt.type='mall'",
				start, range, session, sort, category, user,reverse);
	}

	public SearchResult getTrendingOffer(int start, int range, Session session,
			String sort, String category, User user) throws Exception {
		return paginationOfferQueryExecutor(
				"from offer o join outlet outlt join category cOuter join outletcategory oc "
						+ "Where oc.outlet_id=outlt.id and cOuter.id=oc.category_id and  o.trending = true and o.startDate <= ? and o.endDate >= ? and o.outlet_id=outlt.id",
				start, range, session, sort, category, user,false);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public SearchResult paginationOfferQueryExecutor(String queryStr,
			int start, int range, Session session, String sort,
			String category, User user,boolean reverse) throws Exception {
		SearchResult result = new SearchResult();

		Date startDate = CommonUtil.getIstDateTime();
		Date endDate = CommonUtil.getIstDateTime();

		startDate.setHours(0);
		startDate.setMinutes(0);
		startDate.setSeconds(0);

		endDate.setHours(23);
		endDate.setMinutes(59);
		endDate.setSeconds(59);
		List<Object> offers = new ArrayList<Object>();
		List<Offer> offerNew = new ArrayList<Offer>();
		queryStr = "Select o.detail,o.shortDesc,o.image,outlt.id,outlt.name,outlt.rating,(Select GROUP_CONCAT(im.image ,',') From images im Where im.outlet_id = outlt.id ORDER BY im.outlet_id) as images,(Select GROUP_CONCAT(distinct(c.name) ,',') From category c where c.id=cOuter.id ORDER BY c.name ), o.id as offerId, o.startDate, o.endDate, o.discount, outlt.longitude, outlt.latitude, outlt.address, outlt.phone,outlt.tags "
				+ queryStr;
		if (category != null && category != ""
				&& !category.equalsIgnoreCase("all")) {
			queryStr += " and cOuter.name = '" + category + "'";
		}
		if (sort != null && sort != "" && !sort.equals("relevance"))
			queryStr += " order by " + sort;
		if(reverse)
			queryStr += " DESC";
		Query query = session.createSQLQuery(queryStr).setReadOnly(true)
				.setCacheMode(CacheMode.NORMAL)
				// .setCacheable(true)
				.setParameter(0, CommonUtil.getIstDateTime())
				.setParameter(1, CommonUtil.getIstDateTime());
		offers = query
				./* setFirstResult(start). *//* setMaxResults(range). */list();
		List<Bookmark> bookmarks = null;
		if (user != null) {
			bookmarks = bookmarkDao.fetchBookmarks(user, session);
		}

		if (offers.size() > 0) {
			if (offers.size() % range > 0)
				result.setLastPageNumber((offers.size() / range) + 1);
			else
				result.setLastPageNumber((offers.size() / range));
			List<Category> categoryList = (List<Category>) categoryDao
					.fetchAllCategories(0, 1000, session).getData();
			for (Iterator<Object> iterator = offers.iterator(); iterator
					.hasNext();) {
				Object[] objects = (Object[]) iterator.next();
				Offer offer = new Offer();
				offer.setDetail((String) objects[0]);
				offer.setShortDesc((String) objects[1]);
				offer.setImage((String) objects[2]);
				offer.setOutlet(new Outlet());
				offer.getOutlet().setId((int) objects[3]);
				offer.getOutlet().setName((String) objects[4]);
				offer.getOutlet().setRating(
						((BigDecimal) objects[5]).doubleValue());
				if (bookmarks != null)
					for (Iterator<Bookmark> iterator2 = bookmarks.iterator(); iterator2
							.hasNext();) {
						Bookmark bookmark = iterator2.next();
						if (bookmark.getOutlet().getId() == offer.getOutlet()
								.getId())
							offer.getOutlet().setOutletIsFav(true);
					}

				if (objects[6] != null)
					offer.getOutlet().setImages(
							new ArrayList<String>(Arrays
									.asList(((String) objects[6]).replace(",,",
											",").split("\\s*,\\s*"))));
				offer.getOutlet().setCategory(new HashSet<Category>());
				String[] categories = objects[7] == null ? new String[0]
						: ((String) objects[7]).replace(",,", ",").split(
								"\\s*,\\s*");
				offer.setId((int) objects[8]);
				offer.setStartDate((java.sql.Date) objects[9]);
				offer.setEndDate((java.sql.Date) objects[10]);
				offer.setDiscount((int) objects[11]);
				offer.getOutlet().setLongitude(
						((BigDecimal) objects[12]).doubleValue());
				offer.getOutlet().setLatitude(
						((BigDecimal) objects[13]).doubleValue());
				offer.getOutlet().setAddress((String) objects[14]);
				offer.getOutlet().setPhone(
						((BigInteger) objects[15]).longValue());
				offer.getOutlet().setTags((String) objects[16]);
				if (result.getCategoryList() == null)
					result.setCategoryList(new HashMap<Category, Integer>());

				for (int i = 0; i < categories.length; i++) {
					for (Iterator<Category> iterator2 = categoryList.iterator(); iterator2
							.hasNext();) {
						Category category1 = iterator2.next();
						if (category1.getName().equalsIgnoreCase(categories[i])) {
							offer.getOutlet().getCategory().add(category1);
							if (result.getCategoryList().containsKey(category1)) {
								result.getCategoryList()
										.put(category1,
												result.getCategoryList().get(
														category1) + 1);
							} else
								result.getCategoryList().put(category1, 1);
						}
					}
				}
				offerNew.add(offer);
			}
			if (start == 0)
				start = 1;
			if (offerNew.size() < start)
				result.setData(new ArrayList<Offer>());
			else if (offerNew.size() < (start + range))
				result.setData(offerNew.subList(start - 1, offerNew.size()));
			else
				result.setData(offerNew.subList(start - 1, (start + range)));
			return result;
		} else {
			return new SearchResult();
		}
	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public void updateOffer(Offer offer, Session session) {
		session.merge(offer);
		session.flush();
	}

}
