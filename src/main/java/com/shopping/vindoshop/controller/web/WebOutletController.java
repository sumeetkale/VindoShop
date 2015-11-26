package com.shopping.vindoshop.controller.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.shopping.vindoshop.controller.api.ApiOutletController;
import com.shopping.vindoshop.model.Outlet;
import com.shopping.vindoshop.model.Result;
import com.shopping.vindoshop.util.CommonUtil;

@Controller
public class WebOutletController {

	@Autowired
	ApiOutletController apiOutletController;

	/**
	 * Add outlet Rating
	 *
	 * @param outlet
	 * @param request
	 * @param mav
	 * @return
	 */
	@RequestMapping(value = "/addOutletRating", method = RequestMethod.POST, consumes = "application/*")
	@ResponseBody
	public ModelAndView addOutletRating(@ModelAttribute Outlet outlet,
			HttpServletRequest request, ModelAndView mav) {
		try {

			Result result = apiOutletController
					.addOutletRating(request, outlet);
			if (result.isStatus())
				mav.addObject("msg", result.getMsg());
			else
				mav.addObject("error", result.getMsg());
			mav.addObject("outlet",
					apiOutletController
							.fetchOutletInfo(request, outlet.getId()).getData());
		} catch (Exception e) {
			mav.addObject("error", CommonUtil.exceptionHandler(e));
		}
		mav.setViewName("storeDetail");
		return mav;
	}

	public ApiOutletController getApiOutletController() {
		return apiOutletController;
	}

	public void setApiOutletController(ApiOutletController apiOutletController) {
		this.apiOutletController = apiOutletController;
	}

	/**
	 * Get the StoreDetail page for a provided outletId
	 *
	 * @param outletId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/storeDetail", method = RequestMethod.GET)
	public ModelAndView storeDetail(@RequestParam int outletId,
			HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		Result result;
		try {
			result = apiOutletController.fetchOutletInfo(request, outletId);
			model.addObject("outlet", result.getData());
			if (result.isStatus())
				model.addObject("msg", result.getMsg());
			else
				model.addObject("error", result.getMsg());
		} catch (Exception e) {
			model.addObject("error", CommonUtil.exceptionHandler(e));
		}
		return model;

	}

}
