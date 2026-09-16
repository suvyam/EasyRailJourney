package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleTrainStationUpdateReqDto {

    @NotNull(message = "Schedule train station ID is required.")
    private Long id;

    private Long scheduleTrainId;

    private Long stationId;

    private Integer stationSequence;

    private Date arrivalTime;

    private Date departureTime;
    

}