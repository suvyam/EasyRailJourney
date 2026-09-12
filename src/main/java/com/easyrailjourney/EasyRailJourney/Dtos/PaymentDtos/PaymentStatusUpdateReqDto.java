package com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos;

import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentStatus;
import com.easyrailjourney.Validations.CoachValidaters.AtLeastOneNotNull;

import lombok.Data;

@Data
@AtLeastOneNotNull (
    fields={"gatewayTransactionId","name"} 
)
public class PaymentStatusUpdateReqDto {

    private String gatewayTransactionId;
   
    private PaymentStatus status;

    private String failureReason;
}