package com.easyrailjourney.EasyRailJourney.Dtos.UserDtos;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.users.Users;

import lombok.Data;


@Data
public class UserRegisterRespDto {

    Users user;
    ResponseStatus responseStatus;
    String message;

}
