package com.easyrailjourney.EasyRailJourney.repository.trainRepos;


import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainRescheduleEvent;

public interface ScheduleTrainRescheduleEventRepo
        extends JpaRepository<
                ScheduleTrainRescheduleEvent,
                Long> {
}