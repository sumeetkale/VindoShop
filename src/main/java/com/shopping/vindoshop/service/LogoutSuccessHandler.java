package com.shopping.vindoshop.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		if ("true".equals(request.getHeader("X-Ajax-call"))) {
			response.getWriter().print(
					"{success:true, targetUrl : \'"
							+ this.getTargetUrlParameter() + "\'}");
			response.getWriter().flush();
		} else
			super.onLogoutSuccess(request, response, authentication);
	}
}