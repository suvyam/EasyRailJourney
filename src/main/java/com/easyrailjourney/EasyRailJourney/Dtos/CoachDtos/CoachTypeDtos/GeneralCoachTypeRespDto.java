package com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachTypeDtos;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.CoachType;

import lombok.Data;

@Data
public class GeneralCoachTypeRespDto {

    private List<CoachType> coachTypes = new ArrayList<>();
    private CoachType coachType;
    private ResponseStatus responseStatus;
    private String message;
}