package com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainHistoryRepos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory.ScheduleTrainClassHistory;

public interface ScheduleTrainClassHistoryRepo
        extends JpaRepository<ScheduleTrainClassHistory, Long> {

    List<ScheduleTrainClassHistory>
    findByScheduleTrainClassIdOrderByArchivedAtDesc(
            Long scheduleTrainClassId);

    List<ScheduleTrainClassHistory>
    findByScheduleTrainIdOrderByArchivedAtDesc(
            Long scheduleTrainId);
}