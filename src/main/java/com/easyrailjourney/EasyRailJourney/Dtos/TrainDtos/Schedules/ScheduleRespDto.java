package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules;

import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.Trains.TrainStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClass;

import lombok.Data;

@Data
public class ScheduleRespDto {

    private Long id;

    private Long trainId;
    private String departureStationName;
    private String destinationStationName;

    private Date journeyStartTime;
    private Date journeyEstimatedEndTime;
    ScheduleTrainClass scheduleTrainClass;

    private TrainStatus status;
}