package com.jije.boh.core.service.inter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IActionServletService {
	public String getResponseView(HttpServletRequest request, HttpServletResponse response);
	
	public String getResponseJson(HttpServletRequest request, HttpServletResponse response);
}
