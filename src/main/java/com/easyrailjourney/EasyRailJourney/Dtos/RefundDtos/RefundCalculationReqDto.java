package com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefundCalculationReqDto {

    @NotNull (message="Booking id required to calculate refund")
    private Long bookingId;

}