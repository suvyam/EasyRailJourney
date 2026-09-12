package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCoach;

import com.easyrailjourney.EasyRailJourney.enums.GeneralStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleTrainCoachUpdateReqDto {

    @NotNull(message = "Schedule train coach ID is required.")
    private Long id;

    private Long scheduleTrainClassId;

    private Long coachId;

    private String coachPosition;

    private GeneralStatus status;
}