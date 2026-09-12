package com.easyrailjourney.EasyRailJourney.controllers;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos.BookingResponseDto;
import com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos.CancelBookingReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos.CreateBookingReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos.GeneralBookingReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos.GeneralBookingRespDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.services.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(
            BookingService bookingService) {

        this.bookingService = bookingService;
    }

    // =========================================================
    // VALIDATE BOOKING
    // =========================================================

    @PostMapping("/validate")
    public ResponseEntity<GeneralBookingRespDto> validateBooking(
            @Valid @RequestBody GeneralBookingReqDto reqDto) {

        GeneralBookingRespDto response =
                new GeneralBookingRespDto();

        try {

            bookingService.validation(reqDto);

            response.setResponseStatus(
                    ResponseStatus.SUCCESS
            );

            response.setBooking(
                    List.of()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.setResponseStatus(
                    ResponseStatus.FAILURE
            );

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    // =========================================================
    // CREATE BOOKING
    // =========================================================

    @PostMapping
    public ResponseEntity<GeneralBookingRespDto> createBooking(
            @Valid @RequestBody CreateBookingReqDto reqDto) {

        GeneralBookingRespDto response =
                new GeneralBookingRespDto();

        try {

            Bookings booking =
                    bookingService.createBooking(reqDto);

            BookingResponseDto bookingResponse =
                    bookingService.convertToResponse(
                            booking
                    );

            response.setBooking(
                    List.of(bookingResponse)
            );

            response.setResponseStatus(
                    ResponseStatus.SUCCESS
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (Exception e) {

            response.setResponseStatus(
                    ResponseStatus.FAILURE
            );
            response.setMessage(e.getMessage()+" ");

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    // =========================================================
    // CANCEL BOOKING
    // =========================================================

    @PostMapping("/cancel")
    public ResponseEntity<GeneralBookingRespDto> cancelBooking(
            @Valid @RequestBody CancelBookingReqDto reqDto) {

        GeneralBookingRespDto response =
                new GeneralBookingRespDto();

        try {

            bookingService.cancelBooking(
                    reqDto.getBookingId()
            );

            response.setResponseStatus(
                    ResponseStatus.SUCCESS
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.setResponseStatus(
                    ResponseStatus.FAILURE
            );
            response.setMessage(e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }
}