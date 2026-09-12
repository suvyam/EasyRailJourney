package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ScheduleDeleteReqDto {

    @NotNull(message = "Schedule ID is required.")
    Long id;

    @NotBlank(message = "Schedule deletion reason is required.")
    String scheduleDeleteReason;
}