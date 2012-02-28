package com.jije.boh.core.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.jije.boh.core.domain.servlet.ServletConfig;
import com.jije.boh.core.service.inter.IOSGIFreeMarkerViewResolver;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * Generate the result string through the template ftl file for the default servlet.
 * It only provide for the default servlet.
 * Required value: 
 * 		templatePackage(it will scan this package for the ftl file);
 * 		templateFilePath(which ftl file you want to convert). 
 * @author Alex
 *
 */
public class OSGIFreeMarkerViewResolver implements IOSGIFreeMarkerViewResolver{
	/**
	 * Required Value.
	 * It will scan this package for the ftl file.
	 */
	private String templatePackage;
	
	/**
	 * Option Value.
	 * If not set, it will provide a default configuration.
	 */
	private Configuration configuration;
	
	/**
	 * Required Value.
	 * templateFilePath is a relative file path.
	 * This file will be converted to the string.
	 */
	private String templateFilePath;
	
	/**
	 * Required Value.
	 * When a module bundle call the class, it need to know which bundle call it.
	 */
	private String bundleSymbolicName;

	public OSGIFreeMarkerViewResolver() {
	}

	public String getTemplatePackage() {
		return templatePackage;
	}

	public void setTemplatePackage(String templatePackage) {
		this.templatePackage = templatePackage;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getTemplateFilePath() {
		return templateFilePath;
	}

	public void setTemplateFilePath(String templateFilePath) {
		this.templateFilePath = templateFilePath;
	}

	public String getBundleSymbolicName() {
		return bundleSymbolicName;
	}

	public void setBundleSymbolicName(String bundleSymbolicName) {
		this.bundleSymbolicName = bundleSymbolicName;
	}
	
	public void init(String bundleSymbolicName){
		init(bundleSymbolicName, null);
	}
	
	public void init(String bundleSymbolicName, String templatePackage){
		init(bundleSymbolicName, templatePackage, null);
	}
	
	public void init(String bundleSymbolicName, String templatePackage, Configuration configuration){
		this.templatePackage = templatePackage;
		this.configuration = configuration;
		this.bundleSymbolicName = bundleSymbolicName;
	}

	/**
	 * Provide the default configuration.
	 * @param templatePackage
	 * @return The default configuration.
	 * @throws IOException
	 */
	public Configuration getDefaultConfiguration(String templatePackage) throws IOException {
		//Get the current bundle.
		BundleContext context = FrameworkUtil.getBundle(this.getClass())
				.getBundleContext();
		Bundle bundle = null;
		for(Bundle b : context.getBundles()) {
			if(b.getSymbolicName().equals(bundleSymbolicName)){
				bundle = b;
				break;
			}
		}
		
		//Search the template package path in this bundle.
		Enumeration<URL> bundleTemplates = bundle.findEntries("/", "*", true);
		
		File templateDirFile = null;
		if (StringUtils.isNotEmpty(templatePackage)) {
			String templatePath = "/" + templatePackage.replace(".", "/");
			while (bundleTemplates.hasMoreElements()) {
				URL templateURL = bundleTemplates.nextElement();
				// If it is the folder which you set.
				if (templateURL.getPath().endsWith(templatePath)
						|| templateURL.getPath().endsWith(templatePath + "/")) {
					try {
						// Convert the bundle resource to file resource.
						URLConnection conn = templateURL.openConnection();
						Method method = conn.getClass()
								.getMethod("getLocalURL");
						method.setAccessible(true);
						URL fileUrl = (URL) method.invoke(conn);
						templateDirFile = new File(fileUrl.getFile());
						break;
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		//Set configuration properties.
		Configuration cfg = new Configuration();
		if(templateDirFile != null){
			cfg.setDirectoryForTemplateLoading(templateDirFile);
		}
		// cfg.setLocale(Locale.CHINA);
		cfg.setDefaultEncoding(ServletConfig.SYSTEM_ENCODING);
		return cfg;
	}

	/**
	 * Convert from ftl template file to string.
	 * @param data Type of Map<String, Object> 
	 * @param templateFilePath The ftl file which will be converted.
	 * @return the converted string.
	 * @throws TemplateException
	 * @throws IOException
	 */
	public String generate(Map<String, Object> data, String templateFilePath) throws TemplateException,
			IOException {
		if (data == null) {
			data = new HashMap<String, Object>();
		}

		Configuration configuration = getConfiguration();
		if(configuration == null) {
			configuration = getDefaultConfiguration(getTemplatePackage());
		}
		
		Template template = configuration.getTemplate(templateFilePath);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(byteArrayOutputStream);

		template.process(data, out);
		out.flush();
		String result = new String(byteArrayOutputStream.toByteArray());
		out.close();
		return result;
	}
}
