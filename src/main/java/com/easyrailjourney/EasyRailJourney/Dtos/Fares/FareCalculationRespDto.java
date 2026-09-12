package com.easyrailjourney.EasyRailJourney.Dtos.Fares;

import lombok.Data;

@Data 
public class FareCalculationRespDto {

    private Long trainId;

    private String trainNumber;

    private String trainName;

    private String classType;

    private String strategy;

    private String appliedRule;

    private Double farePerPassenger;

    private Integer passengerCount;

    private Double totalFare;

}