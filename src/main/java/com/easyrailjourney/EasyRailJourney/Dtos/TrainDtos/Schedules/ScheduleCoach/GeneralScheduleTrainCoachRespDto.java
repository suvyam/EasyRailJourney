package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCoach;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainCoach;

import lombok.Data;

@Data
public class GeneralScheduleTrainCoachRespDto {

    private List<ScheduleTrainCoach> scheduleTrainCoaches = new ArrayList<>();
    private ScheduleTrainCoach scheduleTrainCoach;
    private ResponseStatus responseStatus;
    private String message;
}