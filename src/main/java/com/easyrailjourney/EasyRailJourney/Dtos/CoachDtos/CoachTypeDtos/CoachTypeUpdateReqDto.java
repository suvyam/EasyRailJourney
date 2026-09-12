package com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachTypeDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoachTypeUpdateReqDto {

    @NotNull(message = "Coach type ID is required.")
    private Long id;

    @NotBlank(message = "Coach type name is required.")
    private String typeName;
}