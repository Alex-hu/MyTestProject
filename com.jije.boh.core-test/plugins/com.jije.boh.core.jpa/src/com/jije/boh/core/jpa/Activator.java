package com.jije.boh.core.jpa;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
/**
 * Gemini JPA sample activator class
 * 
 * @author Murphy
 */
public class Activator implements BundleActivator{

	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

    
}