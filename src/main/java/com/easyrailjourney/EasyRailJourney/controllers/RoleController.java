package com.easyrailjourney.EasyRailJourney.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.RoleAuthorityDtos.RoleRequestDto;
import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.Role;
import com.easyrailjourney.EasyRailJourney.services.RoleAndAuthorityService.RoleService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/role")

public class RoleController {

    private final RoleService roleService;

    RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping
    @PreAuthorize ("hasAuthority('ADD_ROLES')")
    public ResponseEntity<Role> createRole(
         @Valid  @RequestBody RoleRequestDto dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roleService.createRole(dto));
    }

    @GetMapping
    @PreAuthorize ("hasAuthority('READ_ROLES')")
    public ResponseEntity<List<Role>> getAllRoles() {

        return ResponseEntity.ok(
                roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    @PreAuthorize ("hasAuthority('READ_ROLES')")
    public ResponseEntity<Role> getRoleById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                roleService.getRoleById(id));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize ("hasAuthority('READ_ROLES')")
    public ResponseEntity<Role> getRoleByName(
            @PathVariable String name) {

        return ResponseEntity.ok(
                roleService.getRoleByName(name));
    }

    @PutMapping("/{id}")
    @PreAuthorize ("hasAuthority('UPDATE_ROLES')")
    public ResponseEntity<Role> updateRole(
            @PathVariable Long id, @RequestBody RoleRequestDto dto) {

        return ResponseEntity.ok(
                roleService.updateRole(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize ("hasAuthority('DELETE_ROLES')")
    public ResponseEntity<Void> deleteRole(
            @PathVariable Long id) {

        roleService.deleteRole(id);

        return ResponseEntity.noContent().build();
    }
}