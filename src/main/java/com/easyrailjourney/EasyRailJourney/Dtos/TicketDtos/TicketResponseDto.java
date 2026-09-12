package com.easyrailjourney.EasyRailJourney.Dtos.TicketDtos;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.Ticket;

import lombok.Data;


@Data 
public class TicketResponseDto {

    Ticket ticket;
    ResponseStatus status;
    String message;

}
