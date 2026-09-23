package com.easyrailjourney.EasyRailJourney.Dtos;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;

import lombok.Data;

@Data 
public class ErrorResponseDto {

    ResponseStatus responseStatus;
    String message ;
    
}
