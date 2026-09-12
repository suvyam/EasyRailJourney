package com.easyrailjourney.EasyRailJourney.Dtos.StationDtos;

import com.easyrailjourney.EasyRailJourney.enums.Trains.StationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StationUpdateReqDto {

    @NotNull(message="station id is required")
    private Long id;

    private String code;
    private String name;

    private Long city;

    private Float latitude;
    private Float longitude;

    private StationStatus status;
}