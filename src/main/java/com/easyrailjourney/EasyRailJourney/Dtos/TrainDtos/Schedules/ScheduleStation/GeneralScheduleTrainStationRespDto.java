package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation;
import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainStation;

import lombok.Data;

@Data
public class GeneralScheduleTrainStationRespDto {

    private List<ScheduleTrainStation> scheduleTrainStations = new ArrayList<>();
    private ScheduleTrainStation scheduleTrainStation;
    private ResponseStatus responseStatus;
    private String message;
    private Boolean isCreated;
}