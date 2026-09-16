package com.easyrailjourney.EasyRailJourney.Dtos.UserDtos;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegisterReqDto {

    @NotBlank(message="Full Name required")
    String fullName;

    @NotBlank(message="Profile Name required")
    String profileName;

    @NotBlank(message="Email required")
    String email;

    @NotBlank(message="Password required")
    String password;

    @NotBlank(message="ConfirmPassword required")
    String ConfirmPassword;

    @NotBlank(message="PhoneNumber required")
    String phoneNumber;

    @NotNull(message="ConfirmPassword required")
    Date DOB;

    @NotBlank(message="Gender required")
    String Gender;

    boolean isDeleted;
}
