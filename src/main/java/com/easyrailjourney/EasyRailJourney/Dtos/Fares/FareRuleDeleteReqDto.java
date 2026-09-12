package com.easyrailjourney.EasyRailJourney.Dtos.Fares;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FareRuleDeleteReqDto {

    @NotBlank 
    private Long id;

    @NotBlank 
    private String fareRuleDeleteReason;
}