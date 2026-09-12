package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Seats;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Seat;

import lombok.Data;


@Data
public class GeneralSeatRespDto {
    
    List<Seat> seats = new ArrayList<>();
    Seat seat;
    ResponseStatus responseStatus;
    String message;
}
