package com.easyrailjourney.EasyRailJourney.controllers.ScheduleControllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.ScheduleTrainClassSeatHistoryRespDto;
import com.easyrailjourney.EasyRailJourney.services.trainServices.ScheduleTrainClassSeatHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule-train-class-seat-history")
@RequiredArgsConstructor
public class ScheduleTrainClassSeatHistoryController {

    private final ScheduleTrainClassSeatHistoryService
            scheduleTrainClassSeatHistoryService;

    @GetMapping("/schedule-train-class/{scheduleTrainClassId}")
      @PreAuthorize ("hasAuthority('READ_SCHEDULE_TRAIN_CLASS_SEAT_HISTORY')")
    public List<ScheduleTrainClassSeatHistoryRespDto>
    getByScheduleTrainClassId(
            @PathVariable Long scheduleTrainClassId) {

        return scheduleTrainClassSeatHistoryService
                .getByScheduleTrainClassId(
                        scheduleTrainClassId);
    }
}