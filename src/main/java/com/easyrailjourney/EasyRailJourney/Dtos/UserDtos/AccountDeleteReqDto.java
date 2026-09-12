package com.easyrailjourney.EasyRailJourney.Dtos.UserDtos;

import lombok.Data;

@Data
public class AccountDeleteReqDto {

    long id;
    String email;
    String AccountDeleteReason; 
    
}
