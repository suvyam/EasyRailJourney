package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules;

import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.Trains.TrainStatus;

import lombok.Data;

@Data
public class ScheduleTrainHistoryRespDto {

    private Long id;
    private Long scheduleTrainId;
    private Long trainId;
    private Date journeyStartTime;
    private Date journeyEstimatedEndTime;
    private Long departureStationId;
    private Long destinationStationId;
    private TrainStatus status;
    private Date archivedAt;
}