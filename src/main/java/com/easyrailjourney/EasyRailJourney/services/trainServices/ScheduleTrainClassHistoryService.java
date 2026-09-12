package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleTrainClassHistoryRespDto;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory.ScheduleTrainClassHistory;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClass;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainHistoryRepos.ScheduleTrainClassHistoryRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleTrainClassHistoryService {

    private final ScheduleTrainClassHistoryRepo
            scheduleTrainClassHistoryRepo;

    public void archive(
            ScheduleTrainClass scheduleTrainClass) {

        ScheduleTrainClassHistory history =
                new ScheduleTrainClassHistory();

        history.setScheduleTrainClassId(
                scheduleTrainClass.getId());

        history.setScheduleTrainId(
                scheduleTrainClass
                        .getScheduleTrain()
                        .getId());

        history.setTrainClassId(
                scheduleTrainClass
                        .getTrainClass()
                        .getId());

        history.setArchivedAt(new Date());

        scheduleTrainClassHistoryRepo.save(history);
    }

    public List<ScheduleTrainClassHistoryRespDto>
    getByScheduleTrainId(Long scheduleTrainId) {

        List<ScheduleTrainClassHistory> histories =
                scheduleTrainClassHistoryRepo
                        .findByScheduleTrainIdOrderByArchivedAtDesc(
                                scheduleTrainId);

        List<ScheduleTrainClassHistoryRespDto> response =
                new ArrayList<>();

        for (ScheduleTrainClassHistory history : histories) {

            ScheduleTrainClassHistoryRespDto dto =
                    new ScheduleTrainClassHistoryRespDto();

            dto.setId(history.getId());
            dto.setScheduleTrainClassId(
                    history.getScheduleTrainClassId());
            dto.setScheduleTrainId(
                    history.getScheduleTrainId());
            dto.setTrainClassId(
                    history.getTrainClassId());
            dto.setArchivedAt(
                    history.getArchivedAt());

            response.add(dto);
        }

        return response;
    }
}