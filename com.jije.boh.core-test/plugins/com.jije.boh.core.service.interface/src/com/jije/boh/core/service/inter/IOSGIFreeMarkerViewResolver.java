package com.jije.boh.core.service.inter;

import java.io.IOException;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public interface IOSGIFreeMarkerViewResolver {
	
	//public void init(String bundleSymbolicName);
	
	public void init(String bundleSymbolicName, String templatePackage);
	
	public void init(String bundleSymbolicName, String templatePackage, Configuration configuration);
	
	public String generate(Map<String, Object> data, String templateFilePath)
			throws TemplateException, IOException;
}
