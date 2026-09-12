package com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;

import lombok.Data;


@Data
public class GeneralBookingRespDto {

    ResponseStatus responseStatus;
    List<BookingResponseDto> booking = new ArrayList<>();
    String message;
    
}
