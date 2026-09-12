package com.easyrailjourney.EasyRailJourney.Dtos.StatesDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StateUpdateReqDto {

    @NotNull(message = "State ID is required")
    private Long id;

    @NotBlank(message = "State name is required")
    private String name;

    @NotNull(message = "Country ID is required")
    private Long countryId;
}