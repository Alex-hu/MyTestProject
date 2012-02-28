package com.jije.boh.core.service;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.jije.boh.core.domain.servlet.InvokeMethodObject;
import com.jije.boh.core.service.inter.annotation.OSGIController;
import com.jije.boh.core.service.inter.annotation.OSGIRequestMapping;

public class GenerateRequestMapping {
	private String packagePath;
	private static Map<String, InvokeMethodObject> mapping = new HashMap<String, InvokeMethodObject>();
	private static String bundleSymbolicName;
	
	public String getBundleSymbolicName() {
		return bundleSymbolicName;
	}

	public void setBundleSymbolicName(String bundleSymbolicName) {
		GenerateRequestMapping.bundleSymbolicName = bundleSymbolicName;
	}
	
	public void scanClassInPackage(Map<String, InvokeMethodObject> mapping){
		BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		
		Bundle currentBundle = null;
		
		for(Bundle b : context.getBundles()) {
			if(b.getSymbolicName().equals(bundleSymbolicName)){
				currentBundle = b;
				break;
			}
		}
		
		Enumeration<URL> bundleTemplates = currentBundle.findEntries("/", "*.class", true);
		String templatePath = "/" + getBundleSymbolicName().replace(".", "/");
		while (bundleTemplates.hasMoreElements()) {
			URL templateURL = bundleTemplates.nextElement();
			String classFullPath = templateURL.getPath();
			if (classFullPath.indexOf(templatePath) > 0){
				try {
					String className = classFullPath.replace(".class", "").substring(classFullPath.indexOf(templatePath)).replace("/", ".");
					Class<?> c = currentBundle.loadClass(className.substring(1));
					setRequestMapping(c, mapping);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} 
			}
		}
	}

	public GenerateRequestMapping() {
	}

	public Map<String, InvokeMethodObject> getRequestMapping(){
		return mapping;
	}

	public String getPackagePath() {
		return packagePath;
	}

	public Map<String, InvokeMethodObject> process(String packagePath){
		if(StringUtils.isNotEmpty(packagePath) && !packagePath.equals(this.packagePath))
		{
			this.packagePath = packagePath;
			Map<String, InvokeMethodObject> mapping = new HashMap<String, InvokeMethodObject>();
			scanClassInPackage(mapping);
			return mapping;
		}
		return null;
	}

	private void setRequestMapping(Class<?> clazz, Map<String, InvokeMethodObject> mapping) {
		if (clazz.isAnnotationPresent(OSGIController.class)) {
			if (clazz.isAnnotationPresent(OSGIRequestMapping.class)) {
				String[] controllerMapping = clazz.getAnnotation(OSGIRequestMapping.class).value();
				if (controllerMapping.length > 0) {
					String controllerPath = controllerMapping[0].startsWith("/") ? controllerMapping[0] : "/"
							+ controllerMapping[0];
					for (Method method : clazz.getMethods()) {
						if (method.isAnnotationPresent(OSGIRequestMapping.class)) {
							String[] methodMapping = 
									method.getAnnotation(OSGIRequestMapping.class).value();
							for (String path : methodMapping) {
								String fullPath = controllerPath +
										 (path.startsWith("/") ? path : "/" + path);
								
								mapping.put(fullPath, new InvokeMethodObject(fullPath, clazz, method));
							}
						}
					}
				}
			}

			// for(Method method : clazz.getMethods()){
			// if(method.isAnnotationPresent(OSGIRequestMapping.class)) {
			// String[] methodMapping =
			// method.getAnnotation(OSGIRequestMapping.class).value();
			// }
			// }

		}
	}
}
