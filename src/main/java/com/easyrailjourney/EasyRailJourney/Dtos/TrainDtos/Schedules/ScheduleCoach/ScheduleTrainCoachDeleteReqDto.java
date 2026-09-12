package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCoach;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleTrainCoachDeleteReqDto {

    @NotNull(message = "Schedule train coach ID is required.")
    private Long id;

    @NotBlank(message = "Deletion reason is required.")
    private String deleteReason;
}