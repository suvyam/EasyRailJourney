package com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachTypeDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CoachTypeCreateReqDto {

    @NotBlank(message = "Coach type name is required.")
    private String typeName;

    @NotBlank(message = "Type code is required")
    private String typeCode;

    @NotBlank(message = "Description is required")
    private String description;

}