package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;

import lombok.Data;

@Data
public class GeneralScheduleTrainClassRespDto {
    private List<ScheduleTrainClassRespDto> scheduleTrainClasses = new ArrayList<>();
    private ScheduleTrainClassRespDto scheduleTrainClass;
    private ResponseStatus responseStatus;
    private String message;
}