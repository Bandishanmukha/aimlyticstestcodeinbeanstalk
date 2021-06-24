package com.aimlytics.gateway.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.aimlytics.gateway.model.AppRoles;

public interface AppRolesRepository extends CrudRepository<AppRoles, Long> {

	List<AppRoles> findByCodeIn(String[] roleCodes);
}
