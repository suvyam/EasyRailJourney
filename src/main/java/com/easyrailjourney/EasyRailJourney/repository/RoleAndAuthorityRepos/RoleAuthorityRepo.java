package com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.RoleAuthority;

public interface RoleAuthorityRepo extends JpaRepository<RoleAuthority, Long> {

    List<RoleAuthority> findByRoleId(Long roleId);

    List<RoleAuthority> findByAuthorityId(Long authorityId);

    boolean existsByRoleIdAndAuthorityId(Long roleId, Long authorityId);
}