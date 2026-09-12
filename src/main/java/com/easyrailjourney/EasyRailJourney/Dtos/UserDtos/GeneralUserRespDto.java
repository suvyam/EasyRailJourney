package com.easyrailjourney.EasyRailJourney.Dtos.UserDtos;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.users.Users;

import lombok.Data;

@Data
public class GeneralUserRespDto {
    List<Users> users = new ArrayList<>();
    ResponseStatus responseStatus;
    String message;
}
