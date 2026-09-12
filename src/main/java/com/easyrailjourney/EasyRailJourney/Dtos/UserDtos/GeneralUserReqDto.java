package com.easyrailjourney.EasyRailJourney.Dtos.UserDtos;

import java.util.List;

import lombok.Data;

@Data
public class GeneralUserReqDto {

    String userEmail;
    List<String> roles;
    List<String> authorities;
    
}
