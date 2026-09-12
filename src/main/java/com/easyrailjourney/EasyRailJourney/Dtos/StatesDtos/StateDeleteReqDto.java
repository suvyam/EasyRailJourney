package com.easyrailjourney.EasyRailJourney.Dtos.StatesDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StateDeleteReqDto {

    @NotNull(message = "State ID is required")
    private Long id;

    @NotBlank(message = "State delete reason is required")
    private String deleteReason;
}