package com.easyrailjourney.EasyRailJourney.services;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.UserRegisterReqDto;
import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.Role;
import com.easyrailjourney.EasyRailJourney.models.users.Users;
import com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos.RoleRepo;
import com.easyrailjourney.EasyRailJourney.repository.UserRepo.UserRepo;

import jakarta.transaction.Transactional;

@Service 
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    public AuthService(UserRepo userRepo,PasswordEncoder passwordEncoder,RoleRepo roleRepo){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    };

    // REGISTER USER
    @Transactional 
    public Users registerUser(UserRegisterReqDto reqDto) throws Exception {

        Optional<Users> optionalUser =
                userRepo.findByEmailAndIsDeleted(
                        reqDto.getEmail(),
                        false
                ); 

        if (optionalUser.isPresent()) {
            throw new Exception("Already exist user");
        }

        // CHECK PASSWORD
        if (reqDto.getPassword() == null ||
                reqDto.getConfirmPassword() == null) {

            throw new Exception("Password and confirm password are required");
        }

        if (!reqDto.getPassword()
                .equals(reqDto.getConfirmPassword())) {

            throw new Exception("Password not match");
        }

        // CHECK PROFILE NAME
        if (reqDto.getProfileName() == null ||
                reqDto.getProfileName().isBlank()) {

            throw new Exception("Profile name is required");
        }

        // CHECK PROFILE NAME ALREADY EXISTS
        Optional<Users> profileNameUser =
                userRepo.findByProfileNameAndIsDeleted(
                        reqDto.getProfileName(),
                        false
                );

        if (profileNameUser.isPresent()) {
            throw new Exception("Profile name already exists");
        }

        Users user = new Users();

        String password = passwordEncoder.encode(reqDto.getPassword());

        user.setFullName(reqDto.getFullName());
        user.setProfileName(reqDto.getProfileName());
        user.setDOB(reqDto.getDOB());
        user.setEmail(reqDto.getEmail());
        user.setGender(reqDto.getGender());
        user.setPassword(password);
        user.setPhoneNumber(reqDto.getPhoneNumber());
        user.setDeleted(reqDto.isDeleted());

           // Default role
        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        user.setRoles(new HashSet<>());
        user.getRoles().add(userRole);

        return userRepo.save(user);

    }
    
}
