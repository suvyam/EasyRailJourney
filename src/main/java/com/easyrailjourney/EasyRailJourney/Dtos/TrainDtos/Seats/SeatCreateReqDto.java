package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Seats;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeatCreateReqDto {

    @NotNull(message = "Coach ID is required.")
    Long coachId;

    @NotBlank(message = "Seat number is required.")
    String seatNumber;

    @NotNull(message = "Seat type ID is required.")
    Long seatTypeId;
}