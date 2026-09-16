package com.easyrailjourney.EasyRailJourney.Dtos.UserDtos;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PassengerUpdateReqstDto {


    @NotBlank (message="ProfileName required")
    String profileName;
    String newProfileName;
    String fullName;
    String oldEmail;
    String email;
    String password;
    String ConfirmPassword;
    String phoneNumber;
    Date DOB;
    String Gender;

    
}
