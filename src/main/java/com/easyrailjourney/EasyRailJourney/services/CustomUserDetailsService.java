package com.easyrailjourney.EasyRailJourney.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.models.users.CustomeUserDetail;
import com.easyrailjourney.EasyRailJourney.models.users.Users;
import com.easyrailjourney.EasyRailJourney.repository.UserRepo.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    
    public UserDetails loadUserByUsername(String profileName)
            throws UsernameNotFoundException {

        Users user =
                userRepo.findByProfileNameAndIsDeleted(
                    profileName,
                    false
                ).orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with profileName: " + profileName
                        )
                );

        return new CustomeUserDetail(user);
    }
}