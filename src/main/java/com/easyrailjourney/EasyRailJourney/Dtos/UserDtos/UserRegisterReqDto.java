package com.easyrailjourney.EasyRailJourney.Dtos.UserDtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserRegisterReqDto {

    String fullName;
    String profileName;
    String email;
    String password;
    String ConfirmPassword;
    String phoneNumber;
    LocalDate DOB;
    String Gender;
    boolean isDeleted;
}
