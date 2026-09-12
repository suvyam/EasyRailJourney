package com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos;

import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundStatus;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class RefundRespDto {

    private Long id;

    private Long bookingId;

    private Double paidAmount;

    private Double refundAmount;

    private Double deductionAmount;

    private RefundStatus refundStatus;

    private Date refundDate;

    private String reason;
}