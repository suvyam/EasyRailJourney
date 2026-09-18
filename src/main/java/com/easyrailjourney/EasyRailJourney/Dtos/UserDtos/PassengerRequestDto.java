package com.easyrailjourney.EasyRailJourney.Dtos.UserDtos;

import lombok.Data;

@Data 
public class PassengerRequestDto {

    private String name;

    private Integer age;

    private String gender;
}