package com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos;


import com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundCalculationType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data 
public class RefundRuleReqDto {


    private Long trainId;

    @NotNull
    @Positive
    private Integer cancellationHours;

    @NotNull
    private RefundCalculationType calculationType;

    @NotNull
    @Positive
    private Double value;

    @NotNull
    @Positive
    private Integer priority;

    private boolean active = true;

}
