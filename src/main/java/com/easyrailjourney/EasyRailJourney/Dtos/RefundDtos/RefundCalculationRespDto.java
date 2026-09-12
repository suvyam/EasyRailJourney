package com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos;

import lombok.Data;

@Data 
public class RefundCalculationRespDto {

    private Long bookingId;

    private Double paidAmount;

    private Double refundAmount;

    private Double deduction;

    private String strategy;

    private String appliedRule;

    String Message;
}