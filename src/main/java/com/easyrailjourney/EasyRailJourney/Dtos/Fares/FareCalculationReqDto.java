package com.easyrailjourney.EasyRailJourney.Dtos.Fares;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainClass;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data 
public class FareCalculationReqDto {

    @NotNull
    private Long trainId;

    @NotNull
    private Long sourceStationId;

    @NotNull
    private Long destinationStationId;

    @NotNull
    private TrainClass classType;

    @NotNull
    private Integer passengerCount;

  
}