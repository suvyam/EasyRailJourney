package com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancelBookingReqDto {

    @NotNull(message = "Booking ID is required.")
    private Long bookingId;

    @NotBlank(message = "Booking cancellation reason is required.")
    private String cancelReason;
}