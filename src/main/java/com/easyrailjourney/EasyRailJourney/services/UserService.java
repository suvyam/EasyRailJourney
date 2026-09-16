package com.easyrailjourney.EasyRailJourney.services;


import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.AccountDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.PassengerUpdateReqstDto;
import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.UserRoleReqstDto;
import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.Role;
import com.easyrailjourney.EasyRailJourney.models.users.Users;
import com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos.AuthoritiesRepo;
import com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos.RoleRepo;
import com.easyrailjourney.EasyRailJourney.repository.UserRepo.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final AuthoritiesRepo authoritiesRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            RoleRepo roleRepo,
            UserRepo userRepo,
            AuthoritiesRepo authoritiesRepo,
            PasswordEncoder passwordEncoder) {

        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.authoritiesRepo = authoritiesRepo;
        this.passwordEncoder = passwordEncoder;
    }



    // GET USER
    public List<Users> getAllUsers() throws Exception {

        List<Users> users = userRepo.findAll();

        return users;
    }


    // SEARCH USER

    public List<Users> searchUser(
            Long id,
            String phoneNumber,
            String fullName,
            String email,
            boolean isDeleted) {

        return userRepo.searchUser(
                id,
                phoneNumber,
                fullName,
                email,
                isDeleted
        );
    }


    // UPDATE USER
        @Transactional
    public boolean updateUser(
            PassengerUpdateReqstDto reqstDto) throws Exception {

        Users user =
                userRepo.findByEmailAndIsDeleted(
                        reqstDto.getOldEmail(),
                        false
                ).orElseThrow(
                        () -> new Exception("User Not Found")
                );

        if (reqstDto.getDOB() != null) {
            user.setDOB(reqstDto.getDOB());
        }

        if (reqstDto.getFullName() != null) {
            user.setFullName(reqstDto.getFullName());
        }

        if (reqstDto.getGender() != null) {
            user.setGender(reqstDto.getGender());
        }

        if (reqstDto.getOldEmail() != null &&
                reqstDto.getEmail() != null) {

            if (!user.getEmail().equals(reqstDto.getOldEmail())) {
                throw new Exception("Email not match");
            }

            Optional<Users> userOptional =
                    userRepo.findByEmailAndIsDeleted(
                            reqstDto.getEmail(),
                            false
                    );

            if (userOptional.isPresent() &&
                    !userOptional.get().getId().equals(user.getId())) {

                throw new Exception(
                        "User already registered with new Email address"
                );
            }

            user.setEmail(reqstDto.getEmail());
        }

        if (reqstDto.getPassword() != null &&
                reqstDto.getConfirmPassword() != null) {

            if (!reqstDto.getPassword()
                    .equals(reqstDto.getConfirmPassword())) {

                throw new Exception("Password not match");
            }


            String passwod = passwordEncoder.encode(reqstDto.getPassword());

            // BCrypt password hashing should be done here

            user.setPassword(passwod);
        }

        if (reqstDto.getPhoneNumber() != null) {
            user.setPhoneNumber(reqstDto.getPhoneNumber());
        }

        userRepo.save(user);

        return true;
    }


    // DELETE SOFT USER
    @Transactional
    public boolean deleteUser(
            AccountDeleteReqDto reqDto) throws Exception {

        Optional<Users> userOptional =
                userRepo.findByEmailAndIsDeleted(
                        reqDto.getEmail(),
                        false
                );

        if (userOptional.isEmpty()) {
            throw new Exception("User Not Found");
        }

        Users user = userOptional.get();

        user.setDeleted(true);

        userRepo.save(user);

        return true;
    }


    // DELETE PERMANENTLY USER
    @Transactional
    @PreAuthorize ("hasAuthority('DELETE_USER_PERMANENTLY')")
    public boolean deleteUserPermanently(
            AccountDeleteReqDto reqDto) throws Exception {

        Optional<Users> userOptional =
                userRepo.findByEmail(
                        reqDto.getEmail()
                );

        if (userOptional.isEmpty()) {
            throw new Exception("User Not Found");
        }

        Users user = userOptional.get();

        userRepo.delete(user);

        return true;
    }



    @Transactional
    public boolean addUserRole(
            UserRoleReqstDto reqstDto) throws Exception {

        System.err.println(
                reqstDto.getPassangerEmail()
                        + "-------------------------"
        );

        Users user =
                userRepo.findByEmail(
                        reqstDto.getPassangerEmail()
                ).orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        Role role =
                roleRepo.findById(
                        reqstDto.getRoleId()
                ).orElseThrow(
                        () -> new RuntimeException("Role not found")
                );

        user.getRoles().add(role);

        userRepo.save(user);

        return true;
    }


    @Transactional
    public boolean removeUserRole(
            UserRoleReqstDto reqstDto) throws Exception {

        System.err.println(
                reqstDto.getPassangerEmail()
                        + "-------------------------"
        );

        Users user =
                userRepo.findByEmail(
                        reqstDto.getPassangerEmail()
                ).orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        Role role =
                roleRepo.findById(
                        reqstDto.getRoleId()
                ).orElseThrow(
                        () -> new RuntimeException("Role not found")
                );

        user.getRoles().remove(role);

        userRepo.save(user);

        return true;
    }
}