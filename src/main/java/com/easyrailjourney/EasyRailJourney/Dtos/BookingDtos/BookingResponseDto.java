package com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.easyrailjourney.EasyRailJourney.enums.Bookings.BookingStatus;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentStatus;

import lombok.Data;


@Data 
public class BookingResponseDto {

    private String scheduleTrainName;


    private String sourceStation;


    private String destinationStation;

    private String userName;

    private String trainClassName;

    private Integer numberOfSeats;


    private Date journeyDate;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private Date bookingDate;

    private Map<String,Integer> passangerDetails = new HashMap<>();  // name and age

    private Double totalFare; // update in bookings

    private String pnr;
}
