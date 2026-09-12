package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleTrainStationCreateReqDto {

    @NotNull(message = "Schedule train ID is required.")
    private Long scheduleTrainId;

    @NotNull(message = "Station ID is required.")
    private Long stationId;

    @NotNull(message = "Station Stop sequence is required.")
    private Integer stationSequence;

    @NotNull(message = "Arrival time is required.")
    private Date arrivalTime;

    @NotNull(message = "Departure time is required.")
    private Date departureTime;
}