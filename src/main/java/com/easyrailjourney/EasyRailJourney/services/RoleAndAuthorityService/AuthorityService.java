package com.easyrailjourney.EasyRailJourney.services.RoleAndAuthorityService;


import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.RoleAuthorityDtos.AuthorityRequestDto;
import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.Authorities;
import com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos.AuthoritiesRepo;

@Service
public class AuthorityService {

    private final AuthoritiesRepo authoritiesRepo;

    AuthorityService(AuthoritiesRepo authoritiesRepo) {
        this.authoritiesRepo = authoritiesRepo;
    }

    public Authorities createAuthority(AuthorityRequestDto dto) {

        if (authoritiesRepo.existsByName(dto.getName())) {
            throw new RuntimeException("Authority already exists");
        }

        Authorities authority = new Authorities();

        authority.setName(dto.getName());

        return authoritiesRepo.save(authority);
    }

    public List<Authorities> getAllAuthorities() {
        return authoritiesRepo.findAll();
    }

    public Authorities getAuthorityById(Long id) {

        return authoritiesRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authority not found with id: " + id));
    }

    public Authorities getAuthorityByName(String name) {

        return authoritiesRepo.findByName(name)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authority not found: " + name));
    }

    public Authorities updateAuthority(
            Long id,
            AuthorityRequestDto dto) {

        Authorities authority =
                getAuthorityById(id);

        if (!authority.getName().equals(dto.getName())
                && authoritiesRepo.existsByName(dto.getName())) {

            throw new RuntimeException(
                    "Authority already exists");
        }

        authority.setName(dto.getName());

        return authoritiesRepo.save(authority);
    }

    public void deleteAuthority(Long id) {

        Authorities authority =
                getAuthorityById(id);

        authoritiesRepo.delete(authority);
    }
}