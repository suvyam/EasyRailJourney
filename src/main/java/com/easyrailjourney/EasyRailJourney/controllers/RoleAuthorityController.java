package com.easyrailjourney.EasyRailJourney.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.RoleAuthorityDtos.RoleAuthorityRequestDto;
import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.RoleAuthority;
import com.easyrailjourney.EasyRailJourney.services.RoleAndAuthorityService.RoleAuthorityService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/role-authority")

public class RoleAuthorityController {

    private final RoleAuthorityService roleAuthorityService;

    RoleAuthorityController(RoleAuthorityService roleAuthorityService){
        this.roleAuthorityService = roleAuthorityService;
    }

    @PostMapping
     @PreAuthorize ("hasAuthority('ASSIGN_AUTHORITY_TO_ROLE')")
    public ResponseEntity<RoleAuthority> assignAuthority(
         @Valid  @RequestBody RoleAuthorityRequestDto dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roleAuthorityService.assignAuthority(dto));
    }

    @GetMapping("/role/{roleId}")
    @PreAuthorize ("hasAuthority('READ_ROLE_AUTHORITY')")
    public ResponseEntity<List<RoleAuthority>> getAuthoritiesByRole(
            @PathVariable Long roleId) {

        return ResponseEntity.ok(
                roleAuthorityService
                        .getAuthoritiesByRole(roleId));
    }

    @DeleteMapping("/role/{roleId}/authority/{authorityId}")
    @PreAuthorize ("hasAuthority('DELETE_ROLE_AUTHORITY')")
    public ResponseEntity<Void> removeAuthority(
            @PathVariable Long roleId,
            @PathVariable Long authorityId) {

        roleAuthorityService.removeAuthorityFromRole(
                roleId,
                authorityId);

        return ResponseEntity.noContent().build();
    }
}