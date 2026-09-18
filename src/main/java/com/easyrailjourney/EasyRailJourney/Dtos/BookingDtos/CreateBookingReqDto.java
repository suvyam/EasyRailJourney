package com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.Bookings.BookingStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateBookingReqDto {

    @NotNull(message = "Schedule train ID is required.")
    private Long scheduleTrainId;

    @NotNull(message = " Train Source station ID is required.")
    private Long sourceStationId;

    @NotNull(message = " Train  Destination station ID is required.")
    private Long destinationStationId;

    @NotBlank(message = "User email is required.")
    private String userEmail;

    @NotNull(message = "Schedule train class ID is required.")
    private Long scheduleTrainClassId;

    @Min(value = 1, message = "Number of seats must be at least 1.")
    private Integer numberOfSeats;

    @NotNull(message = "Booking date is required.")
    private Date bookingDate;

    @NotNull(message = "Journey date is required.")
    private Date journeyDate;

    private boolean isDeleted;

    private BookingStatus bookingStatus;

    private String paymentStatus;

    @NotNull (message = "Payment mode required")
    private String paymentMode;

    @NotNull (message = "Payment  method  required")
    private String paymentMethod;

    @NotBlank (message="Please provide train class")
    private String trainClass;

    private Double totalFare;

    private String pnr;

    @NotBlank(message="please add passenger details for booking")
    private List<BookingPassengerReqDto> passengers = new ArrayList<>();

    String senderAccountNumber; 
}