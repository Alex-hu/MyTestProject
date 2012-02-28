package com.jije.boh.core.jpa.test.unit;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.gemini.jpa.provider.PersistenceProvider;
import org.junit.Test;

import com.jije.boh.core.domain.model.Account;
import com.jije.boh.core.domain.model.Customer;

/**
 * JPA sample test, Make sure the database is starting.
 * 
 * @author Murphy
 */
public class JPATest {
	private static final String PU_NAME = "AccountsTest";
	private EntityManagerFactory emf;
	private EntityManager em;

	public void begin() {
		EntityTransaction transaction = getEntityManager().getTransaction();
		if (transaction.isActive()) {
			throw new RuntimeException("TX already active");
		} else {
			transaction.begin();
		}
	}

	public void commit() {
		EntityTransaction transaction = getEntityManager().getTransaction();
		if (transaction.isActive()) {
			transaction.commit();
		} else {
			throw new RuntimeException("TX not active");
		}
		begin();
	}

	public Object getRoot() {
		return this;
	}

	@Test
	public void testJPA() {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();

		Customer c = new Customer("Chan", "Jackie",
				"1034 KingFu Lane, Los Angeles, CA");
		em.persist(c);
		Account a = new Account(c);
		a.setBalance(100.0);
		em.persist(a);

		em.getTransaction().commit();

		TypedQuery<Account> q = em.createQuery("SELECT a FROM Account a",
				Account.class);
		List<Account> results = q.getResultList();
		System.out.println("\n*** Account Report ***");
		assert (results.size() > 0);
		for (Account acct : results) {
			System.out.println("Account: " + acct);
		}
		em.close();
	}

	private EntityManager getEntityManager() {
		if (em == null) {
			em = getEntityManagerFactory().createEntityManager();
		}
		return em;
	}

	private EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			HashMap properties = new HashMap();
			properties.put(PersistenceUnitProperties.CLASSLOADER, this
					.getClass().getClassLoader());
			emf = new PersistenceProvider().createEntityManagerFactory(PU_NAME,
					properties);
		}
		return emf;
	}

	public void dispose() {
		if (getEntityManager().getTransaction().isActive()) {
			getEntityManager().getTransaction().rollback();
		}
		getEntityManagerFactory().close();
	}
}
