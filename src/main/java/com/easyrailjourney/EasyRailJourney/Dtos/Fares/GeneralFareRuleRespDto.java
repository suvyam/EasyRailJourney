package com.easyrailjourney.EasyRailJourney.Dtos.Fares;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;

import lombok.Data;

@Data
public class GeneralFareRuleRespDto {

    private List<FareRuleRespDto> fareRules = new ArrayList<>();
    private FareRuleRespDto fareRule;

    private ResponseStatus responseStatus;
    private String message;
}