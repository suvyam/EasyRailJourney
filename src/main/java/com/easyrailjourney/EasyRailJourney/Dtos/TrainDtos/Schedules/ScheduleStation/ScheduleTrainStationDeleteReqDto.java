package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleTrainStationDeleteReqDto {

    @NotNull(message = "Schedule train station ID is required.")
    private Long id;

    @NotBlank(message = "Deletion reason is required.")
    private String deleteReason;
}