package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleFareRule;

import lombok.Data;

@Data
public class ScheduleTrainFareRuleResDto {

    private Long id;


    private Long scheduleTrainId;

    private Long fareRuleId;
}