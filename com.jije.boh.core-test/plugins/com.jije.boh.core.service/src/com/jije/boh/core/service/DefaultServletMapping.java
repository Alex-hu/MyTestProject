package com.jije.boh.core.service;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jije.boh.core.domain.servlet.InvokeMethodObject;
import com.jije.boh.core.domain.servlet.ServletConfig;
import com.jije.boh.core.service.inter.IActionServletService;
import com.jije.boh.core.service.inter.annotation.OSGIResponseBody;

import freemarker.template.TemplateException;

public class DefaultServletMapping implements IActionServletService{
	private String servicePath;
	private String templatePath;
	private static Map<String, InvokeMethodObject> mapping;
	private static OSGIFreeMarkerViewResolver resolver;
	private static String currentBundleSymbolicName;
	
	public DefaultServletMapping() {
	}

	public Map<String, InvokeMethodObject> getRequestMapping(){
		if(mapping == null || mapping.size() == 0){
			mapping = new HashMap<String, InvokeMethodObject>();
			if(StringUtils.isNotEmpty(servicePath)){
				GenerateRequestMapping generate = new GenerateRequestMapping();
				generate.setBundleSymbolicName(currentBundleSymbolicName);
				mapping = generate.process(servicePath);
			}
		}
		return mapping;
	}
	
	public OSGIFreeMarkerViewResolver getViewResolver(){
		if(resolver == null){
			if(StringUtils.isNotEmpty(templatePath)) {
				resolver = new OSGIFreeMarkerViewResolver();
				resolver.setBundleSymbolicName(currentBundleSymbolicName);
				resolver.setTemplatePackage(templatePath);
			}
		}
		return resolver;
	}

	@Override
	public String getResponseView(HttpServletRequest request,
			HttpServletResponse response) {
//		String fullPath = request.getPathInfo();
//		String[] path = fullPath.split("/");
//		String key = "/";
//		String result = "";
//		
//		if(path.length == 5){
//			key += path[3] + "/" + path[4];
//			
//			if(StringUtils.isEmpty(currentBundleSymbolicName)){
//				currentBundleSymbolicName = ServletConfig.BUNDLE_SYMBOLIC_PRE_NAME + path[2];
//			}
//		}
		
		if(StringUtils.isEmpty(currentBundleSymbolicName)){
			String moduleName = request.getParameter(ServletConfig.REQUEST_PARAMETER_MODULE_NAME);
			currentBundleSymbolicName = ServletConfig.BUNDLE_SYMBOLIC_PRE_NAME + moduleName;
		}
		StringBuilder key = new StringBuilder("/");
		key.append(request.getParameter(ServletConfig.REQUEST_PARAMETER_CONTROLLER_NAME));
		key.append("/");
		key.append(request.getParameter(ServletConfig.REQUEST_PARAMETER_FUNCTION_NAME));
		
		String result = "";
		if(getRequestMapping().containsKey(key.toString())){
			InvokeMethodObject matedata = getRequestMapping().get(key.toString());
			
			try {
				Object o = matedata.getClazz().newInstance();
				Map<String, Object> data = new HashMap<String, Object>(); 
				Method method = matedata.getMethod();
				
				if(method.isAnnotationPresent(OSGIResponseBody.class)) {
					result = JsonService.getJsonDetail(invoke(method, o, data, request, response));
					return result;
				}
				
				Class<?> returnType = method.getReturnType();
				if(returnType.equals(String.class)){
					Object tempObject = invoke(method, o, data, request, response);
					if(tempObject != null){
						String templatePath = "/" + (String)tempObject + ".ftl";
						result = getViewResolver().generate(data, templatePath);
					}
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public String getResponseJson(HttpServletRequest request,
			HttpServletResponse response) {
		return getResponseView(request, response);
	}
	
	public static Object invoke(Method method, 
			Object newInstance, 
			Map<String, Object> data, 
			HttpServletRequest request,
			HttpServletResponse response) {
		Class<?>[] parTypes = method.getParameterTypes();
		List<Object> args = new ArrayList<Object>();
		Object result = null;
		for(Class<?> clazz : parTypes){
			if(clazz.equals(Map.class)){
				args.add(data);
			}
			if(clazz.equals(HttpServletRequest.class)){
				args.add(request);
			}
			if(clazz.equals(HttpServletResponse.class)){
				args.add(response);
			}
		}
		try {
			if(args.size() == 0) {
				result = method.invoke(newInstance);
			}
			else {
				result = method.invoke(newInstance, args.toArray());
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
}
