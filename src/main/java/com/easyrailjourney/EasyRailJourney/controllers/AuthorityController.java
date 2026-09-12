package com.easyrailjourney.EasyRailJourney.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.RoleAuthorityDtos.AuthorityRequestDto;
import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.Authorities;
import com.easyrailjourney.EasyRailJourney.services.RoleAndAuthorityService.AuthorityService;


@RestController
@RequestMapping("/authority")

public class AuthorityController {

    private final AuthorityService authorityService;

    AuthorityController(AuthorityService authorityService){
        this.authorityService = authorityService;
    }


    @PostMapping
    public ResponseEntity<Authorities> createAuthority(
            @RequestBody AuthorityRequestDto dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authorityService.createAuthority(dto));
    }

    @GetMapping
    public ResponseEntity<List<Authorities>> getAllAuthorities() {

        return ResponseEntity.ok(
                authorityService.getAllAuthorities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Authorities> getAuthorityById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                authorityService.getAuthorityById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Authorities> getAuthorityByName(
            @PathVariable String name) {

        return ResponseEntity.ok(
                authorityService.getAuthorityByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Authorities> updateAuthority(
            @PathVariable Long id,
           @RequestBody AuthorityRequestDto dto) {

        return ResponseEntity.ok(
                authorityService.updateAuthority(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthority(
            @PathVariable Long id) {

        authorityService.deleteAuthority(id);

        return ResponseEntity.noContent().build();
    }
}