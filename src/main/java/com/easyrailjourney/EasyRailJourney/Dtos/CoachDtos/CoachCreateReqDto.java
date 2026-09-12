package com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoachCreateReqDto {

    @NotBlank(message = "Coach number is required.")
    private String coachNumber;

    @NotNull(message = "Coach type ID is required.")
    private Long coachTypeId;
}