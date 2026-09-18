package com.easyrailjourney.EasyRailJourney.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PassengerDto {

    @NotBlank(message = "Passenger name is required")
    private String name;

    @NotNull(message = "Passenger age is required")
    private Integer age;

    @NotBlank(message = "Passenger gender is required")
    private String gender;
}