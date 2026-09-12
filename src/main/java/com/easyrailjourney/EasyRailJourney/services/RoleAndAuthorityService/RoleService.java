package com.easyrailjourney.EasyRailJourney.services.RoleAndAuthorityService;



import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.RoleAuthorityDtos.RoleRequestDto;
import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.Role;
import com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos.RoleRepo;

@Service
public class RoleService {

    private final RoleRepo roleRepo;

    RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role createRole(RoleRequestDto dto) {

        if (roleRepo.existsByName(dto.getName())) {
            throw new RuntimeException("Role already exists");
        }

        Role role = new Role();

        role.setName(dto.getName());

        return roleRepo.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    public Role getRoleById(Long id) {

        return roleRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Role not found with id: " + id));
    }

    public Role getRoleByName(String name) {

        return roleRepo.findByName(name)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Role not found: " + name));
    }

    public Role updateRole(Long id, RoleRequestDto dto) {

        Role role = getRoleById(id);

        if (!role.getName().equals(dto.getName())
                && roleRepo.existsByName(dto.getName())) {

            throw new RuntimeException("Role already exists");
        }

        role.setName(dto.getName());

        return roleRepo.save(role);
    }

    public void deleteRole(Long id) {

        Role role = getRoleById(id);

        roleRepo.delete(role);
    }
}