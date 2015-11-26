package com.shopping.vindoshop.controller.api;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.MustJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.hibernate.search.spatial.DistanceSortField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopping.vindoshop.dao.BookmarkDao;
import com.shopping.vindoshop.dao.OutletDao;
import com.shopping.vindoshop.dao.UserDao;
import com.shopping.vindoshop.model.Offer;
import com.shopping.vindoshop.model.SearchResult;
import com.shopping.vindoshop.model.User;
import com.shopping.vindoshop.util.CommonUtil;

@Controller
@RequestMapping("/api")
public class ApiSearchController {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	OutletDao outletDao;

	@Autowired
	BookmarkDao bookmarkDao;

	@Autowired
	UserDao userDao;

	public OutletDao getOutletDao() {
		return outletDao;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public @ResponseBody SearchResult search(HttpServletRequest request,
			@RequestParam String queryStr, @RequestParam int start,
			@RequestParam int range, @RequestParam String location,
			@RequestParam String sort, @RequestParam boolean reverse,
			@RequestParam String category, @RequestParam double longitude,
			@RequestParam double latitude) {
		SearchResult result = new SearchResult();
		Session session = CommonUtil.getSession(sessionFactory);
		try {
			FullTextSession fullTextSession = org.hibernate.search.Search
					.getFullTextSession(session);
			fullTextSession.createIndexer().batchSizeToLoadObjects(25)
					.cacheMode(CacheMode.NORMAL).threadsToLoadObjects(5)
					.threadsForSubsequentFetching(1).startAndWait();
			Transaction transaction = session.beginTransaction();
			QueryBuilder qb = fullTextSession.getSearchFactory()
					.buildQueryBuilder().forEntity(Offer.class).get();
			MustJunction mustjunctions = qb.bool().must(
					qb.keyword()
							.onFields("outlet.name", "outlet.tags", "detail")
							.matching(queryStr).createQuery());

			if (!location.equals("default")) {
				mustjunctions.must(qb.keyword()
						.onFields("outlet.address", "outlet.city")
						.matching(location).createQuery());
			}
			if (!category.equals("all"))
				mustjunctions.must(qb.keyword()
						.onFields("outlet.category.name").matching(category)
						.createQuery());

			FacetingRequest labelFacetingRequest = qb.facet().name("category")
					.onField("outlet.category.name").discrete()
					.orderedBy(FacetSortOrder.COUNT_ASC)
					.includeZeroCounts(false).createFacetingRequest();

			org.apache.lucene.search.Query query = mustjunctions.createQuery();
			FullTextQuery hibQuery = fullTextSession
					.createFullTextQuery(query, Offer.class)
					.setFirstResult(start).setMaxResults(range);
			hibQuery.getFacetManager().enableFaceting(labelFacetingRequest);
			if (!sort.equals("relevance") && !sort.equals("nearby")) {
				hibQuery.setSort(new org.apache.lucene.search.Sort(
						new SortField(sort, CommonUtil.sortList().get(sort),
								reverse)));
			} else if (sort.equals("nearby")) {
				hibQuery.setSort(new Sort(new DistanceSortField(latitude,
						longitude, "location")));
			}
			List<?> data = hibQuery.list();
			result.setData(data);
			List<Facet> facets = hibQuery.getFacetManager().getFacets(
					"category");
			int counter = 0;
			for (Iterator<Facet> iterator = facets.iterator(); iterator
					.hasNext();) {
				Facet facet = iterator.next();
				result.getCategoryList()
						.put(outletDao.fetchCategoryByName(facet.getValue(),
								session), facet.getCount() - 1);
				counter += facet.getCount() - 1;
			}
			if (counter % range > 0)
				result.setLastPageNumber((counter / range) + 1);
			else
				result.setLastPageNumber((counter / range));
			User user = null;
			if (request.getUserPrincipal() != null) {
				user = userDao.findByUserName(request.getUserPrincipal()
						.getName(), session);

			}
			for (Iterator iterator = data.iterator(); iterator.hasNext();) {
				Offer offer = (Offer) iterator.next();

				offer.getOutlet()
						.setOutletIsFav(
								bookmarkDao.isBookmark(user, offer.getOutlet(),
										session));
			}
			transaction.commit();

			result.setStatus(true);
		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
			session.close();
		}
		return result;
	}

	public void setOutletDao(OutletDao outletDao) {
		this.outletDao = outletDao;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
