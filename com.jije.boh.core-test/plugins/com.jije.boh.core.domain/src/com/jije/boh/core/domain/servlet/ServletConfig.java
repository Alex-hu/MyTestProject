package com.jije.boh.core.domain.servlet;

public class ServletConfig {
	/**
	 * FreeMarker View Resolver use it.
	 */
	public static final String SYSTEM_ENCODING = "UTF-8";
	
	/**
	 * This static variable is for request mapping.
	 * So the request URL can be short.
	 * eg:
	 *     If a bundle(symbolic name: com.jije.boh.module.test) want to receive a request,  
	 *     so the request URL can be: /app/call/module.test/{Controller_Name}/{Funcation_Name}
	 * see ActionServletHandler, DefaultServletMapping.
	 */
	public static final String BUNDLE_SYMBOLIC_PRE_NAME = "com.jije.boh.";
	
	/**
	 * 
	 */
	public static final String PRE_URL_OF_RESOURCE_IN_BUNDLE = "moduleresources";
	
	public static final String REQUEST_PARAMETER_MODULE_NAME = "module";
	
	public static final String REQUEST_PARAMETER_CONTROLLER_NAME = "controller";
	
	public static final String REQUEST_PARAMETER_FUNCTION_NAME = "function";
}
