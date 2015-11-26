package com.shopping.vindoshop.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopping.vindoshop.model.ErrorHandler;

@Controller
@RequestMapping
public class ErrorHandlerController {

	@Transactional
	@RequestMapping(value = "/spring_security_login", method = RequestMethod.GET)
	public @ResponseBody ErrorHandler loginError(ModelMap model) {
		ErrorHandler error = new ErrorHandler();
		error.setMsg("invalid user");
		return error;
	}
}
