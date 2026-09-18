package com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos;

import com.easyrailjourney.EasyRailJourney.enums.Gender;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingPassengerReqDto {

    @NotBlank(message = "Passenger name is required.")
    private String name;

    @NotNull(message = "Passenger age is required.")
    @Min(value = 1, message = "Passenger age must be greater than 0.")
    private Integer age;

    @NotNull(message = "Passenger gender is required.")
    private Gender gender;

}