package com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos;

import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentCreateReqDto {


    @NotNull (message="BookingId is required to make Payment")
    private Long bookingId;

    @NotNull (message="UserId is required to make Payment")
    private Long userId;


    @NotNull (message="Amount is required to make Payment")
    private Double amount;


    @NotNull (message="Idemptotency is required to make Payment")
    private String idempotencyKey;


    @NotBlank (message="paymentMethod is required to make Payment")
    private String paymentMethod;

    @NotBlank (message="PaymentMode is required to make Payment")
    private String paymentMode;
    
    @NotBlank (message="ReciverAccountNumber/PhoneNumber is required to make Payment")
    private String reciverAccountNumber;

    @NotBlank (message="SenderAccountNumber/PhoneNumber is required to make Payment")
    private String senderAccountNumber; // if API required or default 

    private PaymentType paymentType; // booking-refund
}