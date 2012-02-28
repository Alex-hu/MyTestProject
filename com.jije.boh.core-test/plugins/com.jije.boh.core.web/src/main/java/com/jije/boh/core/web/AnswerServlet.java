/**
 * 
 */
package com.jije.boh.core.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet uses an OSGi service.
 * This service is declared by a dataaccess bundle, exported and
 * then consumed through the web application Spring context.
 * @author Murphy
 *
 */
public class AnswerServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6670022865795154736L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		answer(resp, req.getMethod());
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		answer(resp, req.getMethod());
	}

	public String getServletInfo() {
		return "Servlet using an OSGi service";
	}

	private void answer(HttpServletResponse response, String method) throws IOException {
		response.setContentType("text/html");

		ServletOutputStream out = response.getOutputStream();
		out.println("<html>");
		out.println("<head><title>Hi, This is the first demo</title></head>");
		out.println("<body>");
		out.println("<h1>WebApp:</h1>");
		out.println("<p>"+2222+"</p>");
		out.println("</body></html>");
	}
	
}
