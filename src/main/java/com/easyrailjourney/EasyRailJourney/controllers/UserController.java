package com.easyrailjourney.EasyRailJourney.controllers;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.AccountDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.GeneralUserRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.PassengerUpdateReqstDto;
import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.UserRoleReqstDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.users.Users;
import com.easyrailjourney.EasyRailJourney.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }


    // GET USER----
    @GetMapping
       @PreAuthorize ("hasAuthority('READ_USER')")
    public ResponseEntity<GeneralUserRespDto> getAllUsers() {

        GeneralUserRespDto respDto = new GeneralUserRespDto();

        try {

            List<Users> users = userService.getAllUsers();

            respDto.setUsers(users);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // SEARCH USER----
    @GetMapping("/search")
    @PreAuthorize ("hasAuthority('SEARCH_USER')")
    public ResponseEntity<GeneralUserRespDto> getUserByIdOrPhoneNumberOrUserNameOrEmail(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "false") boolean isDeleted) {

        GeneralUserRespDto respDto = new GeneralUserRespDto();

        if (id == null &&
                phoneNumber == null &&
                fullName == null &&
                email == null) {

            return ResponseEntity.notFound().build();
        }

        try {

            List<Users> users =
                    userService.searchUser(
                            id,
                            phoneNumber,
                            fullName,
                            email,
                            isDeleted
                    );

            respDto.setUsers(users);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // UPDATE USER----
    @PutMapping
    @PreAuthorize ("hasAuthority('UPDATE_USER')")
    public ResponseEntity<GeneralUserRespDto> updateUser(
        @Valid   @RequestBody PassengerUpdateReqstDto entity) {

        GeneralUserRespDto respDto = new GeneralUserRespDto();

        try {

            boolean ans = userService.updateUser(entity);

            if (ans) {
                respDto.setMessage("Successfully Updated");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);
            } else {
                respDto.setMessage("Not Updated");
                respDto.setResponseStatus(ResponseStatus.FAILURE);
            }

        } catch (Exception e) {

            respDto.setMessage("Failed Updated");
            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.badRequest().body(respDto);
    }

    // DELETE SOFT USER----
    @DeleteMapping
    @PreAuthorize ("hasAuthority('DELETE_USER')")
    public ResponseEntity<GeneralUserRespDto> deleteUser(
        @Valid   @RequestBody AccountDeleteReqDto reqDto) {

        GeneralUserRespDto respDto = new GeneralUserRespDto();

        try {

            boolean ans = userService.deleteUser(reqDto);

            if (!ans) {
                respDto.setMessage("Not Deleted");
                respDto.setResponseStatus(ResponseStatus.FAILURE);
            } else {
                respDto.setMessage("Deleted Successfully");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);
            }

        } catch (Exception e) {

            respDto.setMessage(
                    "Delete Interruption " + e.getMessage()
            );

            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.ok().body(respDto);
    }

    // DELETE PERMANENTLY USER----
    @DeleteMapping("/delete-permanently")
    @PreAuthorize ("hasAuthority('DELETE_USER')")
    public ResponseEntity<GeneralUserRespDto> deleteUserPermanently(
        @Valid   @RequestBody AccountDeleteReqDto reqDto) {

        GeneralUserRespDto respDto = new GeneralUserRespDto();

        try {

            boolean ans =
                    userService.deleteUserPermanently(reqDto);

            if (!ans) {
                respDto.setMessage("Not Deleted");
                respDto.setResponseStatus(ResponseStatus.FAILURE);
            } else {
                respDto.setMessage("Deleted Successfully");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);
            }

        } catch (Exception e) {

            respDto.setMessage(
                    "Delete Interruption " + e.getMessage()
            );

            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.ok().body(respDto);
    }

    // REMOVE ROLE USER----
    @DeleteMapping("/role")
    @PreAuthorize ("hasAuthority('DELETE_ROLE')")
    public ResponseEntity<GeneralUserRespDto> removeUserRole(
        @Valid  @RequestBody UserRoleReqstDto reqstDto) throws Exception {

        GeneralUserRespDto respDto = new GeneralUserRespDto();

        boolean ans =
                userService.removeUserRole(reqstDto);

        if (ans) {
            respDto.setMessage("Successfully Deleted role ");
            respDto.setResponseStatus(ResponseStatus.SUCCESS);
        } else {
            respDto.setMessage("Deletion Failed");
            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.ok().body(respDto);
    }

    // ADD ROLE USER----
    @PostMapping("/role")
    @PreAuthorize ("hasAuthority('ADD_ROLE')")
    public ResponseEntity<GeneralUserRespDto> addUserRole(
        @Valid  @RequestBody UserRoleReqstDto reqstDto) throws Exception {

        GeneralUserRespDto respDto = new GeneralUserRespDto();

        boolean ans =
                userService.addUserRole(reqstDto);

        if (ans) {
            respDto.setMessage("Successfully added role ");
            respDto.setResponseStatus(ResponseStatus.SUCCESS);
        } else {
            respDto.setMessage("Addition Failed");
            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.ok().body(respDto);
    }
}