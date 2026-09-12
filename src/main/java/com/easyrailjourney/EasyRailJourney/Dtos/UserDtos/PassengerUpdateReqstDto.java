package com.easyrailjourney.EasyRailJourney.Dtos.UserDtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PassengerUpdateReqstDto {


    String fullName;
    String oldEmail;
    String email;
    String password;
    String ConfirmPassword;
    String phoneNumber;
    LocalDate DOB;
    String Gender;

    
}
