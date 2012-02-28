package com.jije.boh.core.service.inter;

/**
 * Provide a interface to handle the request mapping.
 * @author Alex
 *
 */
public interface IActionServletHandler {
	/**
	 * Mapping to the right module bundle.
	 * @param path request path.
	 * @return module bundle's service which inherit the IActionServletService.
	 */
	public IActionServletService getActionServlet(String path);
}
