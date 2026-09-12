package com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos;

import java.util.List;
import java.util.ArrayList;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Coach;

import lombok.Data;

@Data
public class GeneralCoachRespDto {

    private List<Coach> coaches = new ArrayList<>();
    private Coach coach;
    private ResponseStatus responseStatus;
    private String message;
}