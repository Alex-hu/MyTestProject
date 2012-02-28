package com.jije.boh.core.jpa.test;

import javax.persistence.*;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

/**
 * Test class to test EMF Service from a client
 * 
 * @author Murphy
 */
public abstract class JpaRunningHelper {

	public static BundleContext context;

	/* === Methods that *must* be subclassed === */

	public abstract EntityManagerFactory getEmf();

	public abstract String getTestPersistenceUnitName();

	/* === Methods that *may* be subclassed === */

	public boolean needsEmfService() {
		return true;
	}

	public boolean needsDsfService() {
		return true;
	}

	public String testName() {
		return this.getClass().getSimpleName();
	}

	/* === Helper methods === */

	public EntityManagerFactory lookupEntityManagerFactory(String testName,
			String puName) {
		String filter = "(osgi.unit.name=" + puName + ")";
		ServiceReference[] refs = null;
		try {
			refs = context.getServiceReferences(
					EntityManagerFactory.class.getName(), filter);
		} catch (InvalidSyntaxException isEx) {
			new RuntimeException("Bad filter", isEx);
		}
		slog(testName, "EMF Service refs looked up from registry: " + refs);
		return (refs == null) ? null : (EntityManagerFactory) context
				.getService(refs[0]);
	}

	public EntityManagerFactoryBuilder lookupEntityManagerFactoryBuilder(
			String testName, String puName) {
		String filter = "(osgi.unit.name=" + puName + ")";
		ServiceReference[] refs = null;
		try {
			refs = context.getServiceReferences(
					EntityManagerFactoryBuilder.class.getName(), filter);
		} catch (InvalidSyntaxException isEx) {
			new RuntimeException("Bad filter", isEx);
		}
		slog(testName, "EMF Builder Service refs looked up from registry: "
				+ refs);
		return (refs == null) ? null : (EntityManagerFactoryBuilder) context
				.getService(refs[0]);
	}

	public static void slog(String testName, String msg) {
		System.out.println("***** " + testName + " - " + msg);
	}

	public void log(String msg) {
		System.out.println("***** " + this.getClass().getSimpleName() + " - "
				+ msg);
	}
}
