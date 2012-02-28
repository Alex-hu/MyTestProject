package com.jije.boh.core.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Sample user class.
 * 
 * @author Murphy
 */
@Entity
public class User extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -2942735936715107252L;

	@Column(unique = true)
	private String username;

	private String firstname;
	private String lastname;

	public User() {

		this(null);
	}

	/**
	 * Creates a new user instance.
	 */
	public User(Long id) {

		this.setId(id);
	}

	/**
	 * Returns the username.
	 * 
	 * @return
	 */
	public String getUsername() {

		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {

		this.username = username;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {

		return firstname;
	}

	/**
	 * @param firstname
	 *            the firstname to set
	 */
	public void setFirstname(String firstname) {

		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {

		return lastname;
	}

	/**
	 * @param lastname
	 *            the lastname to set
	 */
	public void setLastname(String lastname) {

		this.lastname = lastname;
	}
}
