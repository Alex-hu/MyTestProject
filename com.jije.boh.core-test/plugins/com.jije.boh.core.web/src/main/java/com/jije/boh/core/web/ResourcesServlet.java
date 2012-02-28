package com.jije.boh.core.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.jije.boh.core.domain.servlet.ServletConfig;


public class ResourcesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6083748410705646721L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			redirectRescources(req, resp);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
	private void redirectRescources(HttpServletRequest req, HttpServletResponse resp) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException{
		String url = req.getRequestURI();
		url = url.substring(1).replace(ServletConfig.PRE_URL_OF_RESOURCE_IN_BUNDLE, "");
		String[] paths = url.split("/");
		
		String bundleName = ServletConfig.BUNDLE_SYMBOLIC_PRE_NAME + paths[1];
		Bundle currentBundle = getCurrentBundle(bundleName);
		
		Enumeration<URL> bundleTemplates = currentBundle.findEntries("/", paths[paths.length - 1], true);
		File resource = null;
		while (bundleTemplates.hasMoreElements()) {
			URL templateURL = bundleTemplates.nextElement();
			
			URLConnection conn = templateURL.openConnection();
			Method method = conn.getClass().getMethod("getLocalURL");
			method.setAccessible(true);
			URL fileUrl = (URL) method.invoke(conn);
			resource = new File(fileUrl.getFile());
			
			FileInputStream in = null;
			try {
				ServletOutputStream out = resp.getOutputStream();
				in = new FileInputStream(resource);
				byte b[] = new byte[in.available()];
				in.read(b);
				out.write(b);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
//			int n = 0;
//			while ((n = in.read(b)) != -1) {
//				out.write(b, 0, n);
//			}
			break;
		}
	}
	
	private Bundle getCurrentBundle(String bundleSymbolicName){
		BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		Bundle currentBundle = null;
		
		for(Bundle b : context.getBundles()) {
			if(b.getSymbolicName().equals(bundleSymbolicName)){
				currentBundle = b;
				break;
			}
		}
		return currentBundle;
	}
}
