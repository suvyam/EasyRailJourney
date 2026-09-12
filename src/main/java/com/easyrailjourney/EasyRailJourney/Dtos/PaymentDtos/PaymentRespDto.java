package com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos;

import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMethod;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder 
public class PaymentRespDto {

    private Long paymentId;

    private Long bookingId;

    private Double amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus status;

    private String gatewayTransactionId;

    private String gatewayOrderId;

    private String failureReason;
}