package com.easyrailjourney.EasyRailJourney.Dtos.CountryDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CountryDeleteReqDto {

    @NotNull(message = "Country ID is required")
    private Long id;

    @NotBlank(message = "Country delete reason is required")
    private String deleteReason;
}