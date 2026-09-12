package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules;

import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.Trains.TrainStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleCreateReqDto {

    @NotNull(message = "Train ID is required.")
    Long trainId;

    @NotNull(message = "Departure station ID is required.")
    Long departureStationId;

    @NotNull(message = "Destination station ID is required.")
    Long destinationStationId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "Journey start time is required.")
    Date journeyStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "Estimated journey end time is required.")
    Date journeyEstimatedEndTime;

    @NotNull(message = "Schedule status is required.")
    TrainStatus status;
}