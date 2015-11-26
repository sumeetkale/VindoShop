package com.shopping.vindoshop.controller.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shopping.vindoshop.controller.api.ApiOfferController;
import com.shopping.vindoshop.controller.api.ApiSearchController;
import com.shopping.vindoshop.model.Result;
import com.shopping.vindoshop.util.CommonUtil;
import com.shopping.vindoshop.util.Constants;

@Controller
@RequestMapping("/")
public class SearchController {

	@Autowired
	ApiSearchController searchController;
	@Autowired
	ApiOfferController offerController;

	@RequestMapping(value = "/offerListing", method = RequestMethod.GET)
	public ModelAndView offerListing(HttpServletRequest request,
			@RequestParam String type) {

		String query_str = request.getParameter("query");

		String location = request.getParameter("where") == null
				|| request.getParameter("where") == "" ? "default" : request
				.getParameter("where");
		String pageNo = request.getParameter("pageNo") == null
				|| "".equals(request.getParameter("pageNo")) ? "1" : request
				.getParameter("pageNo");
		int startrange = ((Integer.parseInt(pageNo) - 1) * Constants.SEARCH_RANGE) + 1;
		String sort = request.getParameter("sortby") == null ? "relevance"
				: request.getParameter("sortby");
		String category = request.getParameter("category") == null ? "all"
				: request.getParameter("category");
		Boolean reverse = (Boolean) (request.getParameter("reverse")==null?false:request.getParameter("reverse"));
		Result result = new Result();
		Map<String, Result> search_data = new HashMap<String, Result>();

		try {
			Result offerListing = null;
			switch (type) {
			case "search":
				offerListing = searchController.search(request, query_str,
						startrange, Constants.SEARCH_RANGE, location, sort,
						reverse, category, 0.0, 0.0);
				break;
			case "exclusive":
				offerListing = offerController.fetchExclusiveOffer(request,
						startrange, Constants.SEARCH_RANGE, sort, category,reverse);
				break;
			case "mall":
				offerListing = offerController.fetchMallOffer(request,
						startrange, Constants.SEARCH_RANGE, sort, category,reverse);
				break;
			default:
				break;
			}
			search_data.put("offerListing", offerListing);
			result.setData(search_data);
			result.setStatus(true);

		} catch (Exception e) {
			result.setMsg(CommonUtil.exceptionHandler(e));
		} finally {
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("result", result);
		model.put("query", query_str);
		model.put("place", location);
		model.put("category", category);
		model.put("pageNo", pageNo);
		model.put("sortby", sort);
		model.put("type", type);
		model.put("reverse", reverse);

		return new ModelAndView("offerListing", model);
	}
}
