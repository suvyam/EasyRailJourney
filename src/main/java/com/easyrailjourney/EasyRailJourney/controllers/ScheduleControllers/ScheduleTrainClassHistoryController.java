package com.easyrailjourney.EasyRailJourney.controllers.ScheduleControllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleTrainClassHistoryRespDto;
import com.easyrailjourney.EasyRailJourney.services.trainServices.ScheduleTrainClassHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule-train-class-history")
@RequiredArgsConstructor
public class ScheduleTrainClassHistoryController {

    private final ScheduleTrainClassHistoryService
            scheduleTrainClassHistoryService;

    @GetMapping("/schedule-train/{scheduleTrainId}")
    public List<ScheduleTrainClassHistoryRespDto>
    getByScheduleTrainId(
            @PathVariable Long scheduleTrainId) {

        return scheduleTrainClassHistoryService
                .getByScheduleTrainId(scheduleTrainId);
    }
}