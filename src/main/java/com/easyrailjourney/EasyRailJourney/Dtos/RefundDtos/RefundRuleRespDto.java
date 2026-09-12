package com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos;


import com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundCalculationType;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RefundRuleRespDto {

    private Long id;

    private Long trainId;

    private Integer cancellationHours;

    private RefundCalculationType calculationType;

    private Double value;

    private Integer priority;

    private boolean active;

    String message;
}