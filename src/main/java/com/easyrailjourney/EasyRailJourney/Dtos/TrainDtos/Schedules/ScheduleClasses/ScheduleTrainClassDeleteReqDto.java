package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleTrainClassDeleteReqDto {

    @NotNull(message = "Schedule train class ID is required.")
    private Long id;

    @NotBlank(message = "Deletion reason is required.")
    private String deleteReason;
}