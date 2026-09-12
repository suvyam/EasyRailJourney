package com.easyrailjourney.EasyRailJourney.Dtos.StationDtos;

import com.easyrailjourney.Validations.CoachValidaters.AtLeastOneNotNull;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@AtLeastOneNotNull (
    fields={"id","code"}
)
public class StationDeleteReqDto {

    private Long id;
    private String code;

    @NotBlank (message="deletion reason is required")
    private String stationDeleteReason;
}