package com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos;

import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentStatus;

import lombok.Data;

@Data 
public class PaymentStratergyResp {

    String transactionId;
    PaymentStatus status;
    String failReason;
    
}
