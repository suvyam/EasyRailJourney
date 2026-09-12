package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.SeatType;


import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.SeatType;

import lombok.Data;

@Data
public class SeatTypeRespDto {

    private Long id;

    private String typeCode;

    private String typeName;

    private SeatType seatType;

    private String description;

    private boolean isDeleted;

    ResponseStatus responseStatus;

    String message;

    List<SeatType> seatTypes;
}