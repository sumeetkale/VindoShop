package com.shopping.vindoshop.controller.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopping.vindoshop.model.Result;

@Controller
@RequestMapping("/errors")
public class ApiErrorController {

	@RequestMapping("badCredentials")
	@ResponseBody
	public Result badCredentials(HttpServletRequest request) throws Exception {
		return new Result(false, "Invalid Login Credentials");
	}

	@RequestMapping("resourcenotfound")
	@ResponseBody
	public Result resourceNotFound(HttpServletRequest request) throws Exception {
		return new Result(false, "Resource Not Found");
	}

	@RequestMapping("unauthorised")
	@ResponseBody
	public Result unAuthorised(HttpServletRequest request) throws Exception {
		return new Result(false, "Unauthorised Request");
	}

}
