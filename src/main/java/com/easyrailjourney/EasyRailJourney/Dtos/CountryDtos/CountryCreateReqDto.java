package com.easyrailjourney.EasyRailJourney.Dtos.CountryDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CountryCreateReqDto {

    @NotBlank(message = "Country name is required")
    private String name;
}