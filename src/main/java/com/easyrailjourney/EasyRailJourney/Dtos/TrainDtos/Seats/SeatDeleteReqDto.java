package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Seats;

import com.easyrailjourney.Validations.SeatValidaters.ValidSeat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@ValidSeat
public class SeatDeleteReqDto {


    Long id;
    Long coachId;
    String seatNumber;

    @NotBlank 
    String seatDeleteReason;
}
