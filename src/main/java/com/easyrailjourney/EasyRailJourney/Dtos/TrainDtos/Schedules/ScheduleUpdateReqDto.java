package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules;

import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.Trains.TrainStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleUpdateReqDto {

    @NotNull(message = "Schedule ID is required.")
    Long id;

    Long trainId;
    Long departureStationId;
    Long destinationStationId;

    Date journeyStartTime;
    Date journeyEstimatedEndTime;

    TrainStatus status;
}