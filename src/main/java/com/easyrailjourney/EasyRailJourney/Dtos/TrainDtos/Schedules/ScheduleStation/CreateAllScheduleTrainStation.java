package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class CreateAllScheduleTrainStation {

    @NotNull (message="please submit all station for schedule train")
    List<ScheduleTrainStationCreateReqDto> scheduleTrainStations;
}
