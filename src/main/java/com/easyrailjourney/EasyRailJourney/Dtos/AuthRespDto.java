package com.easyrailjourney.EasyRailJourney.Dtos;

import com.easyrailjourney.EasyRailJourney.models.users.RefreshToken;

import lombok.Data;

@Data 
public class AuthRespDto {

    String AccessToken;
    RefreshToken refreshToken;

   
    
}
