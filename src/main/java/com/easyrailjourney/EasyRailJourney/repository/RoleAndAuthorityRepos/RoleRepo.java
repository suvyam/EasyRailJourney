package com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    Optional<Role> findById(Role id);


    boolean existsByName(String name);
}