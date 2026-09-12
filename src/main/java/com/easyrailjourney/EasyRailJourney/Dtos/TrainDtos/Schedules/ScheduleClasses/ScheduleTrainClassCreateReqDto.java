package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ScheduleTrainClassCreateReqDto {

    @NotNull(message = "Schedule train ID is required.")
    private Long scheduleTrainId;

    @NotNull(message = "Train class ID is required.")
    private Long trainClassId;
}