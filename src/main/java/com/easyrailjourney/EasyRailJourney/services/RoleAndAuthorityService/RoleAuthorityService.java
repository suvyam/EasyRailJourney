package com.easyrailjourney.EasyRailJourney.services.RoleAndAuthorityService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.RoleAuthorityDtos.RoleAuthorityRequestDto;
import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.Authorities;
import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.Role;
import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.RoleAuthority;
import com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos.AuthoritiesRepo;
import com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos.RoleAuthorityRepo;
import com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos.RoleRepo;


@Service

public class RoleAuthorityService {

    private final RoleRepo roleRepo;
    private final AuthoritiesRepo authorityRepo;
    private final RoleAuthorityRepo roleAuthorityRepo;

    RoleAuthorityService(RoleRepo roleRepo,AuthoritiesRepo authorityRepo,RoleAuthorityRepo roleAuthorityRepo){
        this.roleRepo = roleRepo;
        this.authorityRepo = authorityRepo;
        this.roleAuthorityRepo = roleAuthorityRepo;
    };

    public RoleAuthority assignAuthority(
            RoleAuthorityRequestDto dto) {

        Role role = roleRepo.findById(dto.getRoleId())
                .orElseThrow(() ->
                        new RuntimeException("Role not found"));

        Authorities authority = authorityRepo
                .findById(dto.getAuthorityId())
                .orElseThrow(() ->
                        new RuntimeException("Authority not found"));

        if (roleAuthorityRepo.existsByRoleIdAndAuthorityId(
                dto.getRoleId(),
                dto.getAuthorityId())) {

            throw new RuntimeException(
                    "Authority already assigned to this role");
        }

        RoleAuthority roleAuthority = new RoleAuthority();

        roleAuthority.setRole(role);
        roleAuthority.setAuthority(authority);

        return roleAuthorityRepo.save(roleAuthority);
    }

    public List<RoleAuthority> getAuthoritiesByRole(Long roleId) {

        if (!roleRepo.existsById(roleId)) {
            throw new RuntimeException("Role not found");
        }

        return roleAuthorityRepo.findByRoleId(roleId);
    }

    public void removeAuthorityFromRole(
            Long roleId,
            Long authorityId) {

        List<RoleAuthority> mappings =
                roleAuthorityRepo.findByRoleId(roleId);

                RoleAuthority mapping = mappings.stream()
                .filter(x -> x.getAuthority().getId() == authorityId)
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authority is not assigned to this role"));

        roleAuthorityRepo.delete(mapping);
    }
}