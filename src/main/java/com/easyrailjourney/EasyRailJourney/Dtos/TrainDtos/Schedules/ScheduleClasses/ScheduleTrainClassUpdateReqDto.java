package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleTrainClassUpdateReqDto {

    @NotNull(message = "Schedule train class ID is required.")
    private Long id; // Schedule train class ID

    private Long scheduleTrainId;

    private Long trainClassId;
}