package com.jije.boh.core.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.jije.boh.core.jpa.domain.User;

/**
 * Repository interface for {@link User} instances. Provides basic CRUD
 * operations due to the extension of {@link JpaRepository}. Includes custom
 * implemented functionality by extending {@link UserRepositoryCustom}.
 * 
 * @author Murphy
 */
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

	@Transactional(readOnly = true)
	List<User> findByLastname(String lastname);

	@Transactional(readOnly = true)
	long count();
}
