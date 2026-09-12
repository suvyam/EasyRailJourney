package com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainHistoryRepos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory.ScheduleTrainCoachHistory;

public interface ScheduleTrainCoachHistoryRepo
        extends JpaRepository<ScheduleTrainCoachHistory, Long> {

    List<ScheduleTrainCoachHistory>
    findByScheduleTrainClassIdOrderByArchivedAtDesc(
            Long scheduleTrainClassId);
}