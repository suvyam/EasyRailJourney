package com.easyrailjourney.EasyRailJourney.Dtos.StationDtos;


import com.easyrailjourney.EasyRailJourney.enums.Trains.StationStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StationCreateReqDto {

    @NotBlank (message="station code is required")
    private String code;

    @NotBlank (message="station name is required")
    private String name;

    @NotBlank (message="station cityId is required")
    private Long city_id;


    @NotNull (message="Latitude is required")
    private String latitude;

    @NotNull (message="Latitude is required")
    private String longitude;

    private StationStatus status;
}