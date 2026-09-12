package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleFareRule;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleTrainFareRuleReqDto {

    @NotNull(message = "Schedule train ID is required.")
    private Long scheduleTrainId;

    @NotNull(message = "Fare rule ID is required.")
    private Long fareRuleId;
}