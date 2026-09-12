package com.easyrailjourney.EasyRailJourney.services.trainServices;




import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.ScheduleTrainCoachHistoryRespDto;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory.ScheduleTrainCoachHistory;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainCoach;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainHistoryRepos.ScheduleTrainCoachHistoryRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleTrainCoachHistoryService {

    private final ScheduleTrainCoachHistoryRepo
            scheduleTrainCoachHistoryRepo;

    public void archive(
            ScheduleTrainCoach scheduleTrainCoach) {

        ScheduleTrainCoachHistory history =
                new ScheduleTrainCoachHistory();

        history.setScheduleTrainCoachId(
                scheduleTrainCoach.getId());

        history.setScheduleTrainClassId(
                scheduleTrainCoach
                        .getScheduleTrainClass()
                        .getId());

        history.setCoachId(
                scheduleTrainCoach
                        .getCoach()
                        .getId());

        history.setCoachPosition(
                scheduleTrainCoach.getCoachPosition());

        history.setStatus(
                scheduleTrainCoach.getStatus());

        history.setArchivedAt(new Date());

        scheduleTrainCoachHistoryRepo.save(history);
    }

    public List<ScheduleTrainCoachHistoryRespDto>
    getByScheduleTrainClassId(
            Long scheduleTrainClassId) {

        List<ScheduleTrainCoachHistory> histories =
                scheduleTrainCoachHistoryRepo
                        .findByScheduleTrainClassIdOrderByArchivedAtDesc(
                                scheduleTrainClassId);

        List<ScheduleTrainCoachHistoryRespDto> response =
                new ArrayList<>();

        for (ScheduleTrainCoachHistory history : histories) {

            ScheduleTrainCoachHistoryRespDto dto =
                    new ScheduleTrainCoachHistoryRespDto();

            dto.setId(history.getId());
            dto.setScheduleTrainCoachId(
                    history.getScheduleTrainCoachId());
            dto.setScheduleTrainClassId(
                    history.getScheduleTrainClassId());
            dto.setCoachId(
                    history.getCoachId());
            dto.setCoachPosition(
                    history.getCoachPosition());
            dto.setStatus(
                    history.getStatus());
            dto.setArchivedAt(
                    history.getArchivedAt());

            response.add(dto);
        }

        return response;
    }
}