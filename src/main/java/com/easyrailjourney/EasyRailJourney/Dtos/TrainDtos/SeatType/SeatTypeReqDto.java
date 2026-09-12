package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.SeatType;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SeatTypeReqDto {

    Long id;

    @NotBlank
    private String typeCode;

    @NotBlank
    private String typeName;

    private String description;
}