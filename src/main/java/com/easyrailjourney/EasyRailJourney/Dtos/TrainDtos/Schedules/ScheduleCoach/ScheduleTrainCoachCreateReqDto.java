package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCoach;

import com.easyrailjourney.EasyRailJourney.enums.GeneralStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleTrainCoachCreateReqDto {

    @NotNull(message = "Schedule train class ID is required.")
    private Long scheduleTrainClassId;

    @NotNull(message = "Coach ID is required.")
    private Long coachId;

    @NotBlank(message = "Coach position is required.")
    private String coachPosition;

    @NotNull(message = "Coach status is required.")
    private GeneralStatus status;
}