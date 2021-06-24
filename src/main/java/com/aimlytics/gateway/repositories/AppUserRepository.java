package com.aimlytics.gateway.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.aimlytics.gateway.model.AppUser;
import org.springframework.transaction.annotation.Transactional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {

	Optional<AppUser> findByUserName(String userName);

	@Modifying
	@Transactional
	@Query("DELETE AppUser au WHERE au.refId = :refId")
	void deleteByRefId(String refId);

	Optional<AppUser> findByRefId(String userUid);

}
