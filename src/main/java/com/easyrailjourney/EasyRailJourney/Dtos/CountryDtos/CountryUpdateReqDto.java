package com.easyrailjourney.EasyRailJourney.Dtos.CountryDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CountryUpdateReqDto {

    @NotNull(message = "Country ID is required")
    private Long id;

    @NotBlank(message = "Country name is required")
    private String name;
}