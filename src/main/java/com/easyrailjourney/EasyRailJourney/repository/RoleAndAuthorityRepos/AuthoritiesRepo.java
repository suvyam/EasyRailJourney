package com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.Authorities;

public interface AuthoritiesRepo extends JpaRepository<Authorities, Long> {

    Optional<Authorities> findByName(String name);

    boolean existsByName(String name);
}