package com.easyrailjourney.EasyRailJourney.services;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.AccountDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.PassengerUpdateReqstDto;
import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.UserRegisterReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.UserRoleReqstDto;
import com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys.Role;
import com.easyrailjourney.EasyRailJourney.models.users.Users;
import com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos.AuthoritiesRepo;
import com.easyrailjourney.EasyRailJourney.repository.RoleAndAuthorityRepos.RoleRepo;
import com.easyrailjourney.EasyRailJourney.repository.UserRepo.UserRepo;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final AuthoritiesRepo authoritiesRepo;
    private final RoleRepo roleRepo;

    public UserService(
            RoleRepo roleRepo,
            UserRepo userRepo,
            AuthoritiesRepo authoritiesRepo) {

        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.authoritiesRepo = authoritiesRepo;
    }

    // REGISTER USER
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
                userRepo.findByProfileName(
                        reqDto.getProfileName()
                );

        if (profileNameUser.isPresent()) {
            throw new Exception("Profile name already exists");
        }

        Users user = new Users();

        user.setFullName(reqDto.getFullName());
        user.setProfileName(reqDto.getProfileName());
        user.setDOB(reqDto.getDOB());
        user.setEmail(reqDto.getEmail());
        user.setGender(reqDto.getGender());
        user.setPassword(reqDto.getPassword());
        user.setPhoneNumber(reqDto.getPhoneNumber());
        user.setDeleted(reqDto.isDeleted());

        System.err.println("First Change");

        System.err.println("Second Change");

        return userRepo.save(user);
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

            // BCrypt password hashing should be done here
            user.setPassword(reqstDto.getPassword());
        }

        if (reqstDto.getPhoneNumber() != null) {
            user.setPhoneNumber(reqstDto.getPhoneNumber());
        }

        userRepo.save(user);

        return true;
    }


    // DELETE SOFT USER
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


    // ADD ROLE USER
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


    // REMOVE ROLE USER
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