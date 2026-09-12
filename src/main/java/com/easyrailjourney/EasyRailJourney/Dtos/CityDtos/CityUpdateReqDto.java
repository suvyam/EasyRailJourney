package com.easyrailjourney.EasyRailJourney.Dtos.CityDtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CityUpdateReqDto {

    @NotNull(message = "City ID is required.")
    private Long id;

    @NotBlank(message = "City name is required.")
    private String name;

    @NotNull(message = "State ID is required.")
    private Long stateId;
}