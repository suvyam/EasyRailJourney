package com.easyrailjourney.EasyRailJourney.repository.trainRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.enums.GeneralStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Coach;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrain;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClass;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainCoach;

public interface ScheduleTrainCoachRepo
        extends JpaRepository<ScheduleTrainCoach, Long> {

    Optional<ScheduleTrainCoach>
    findByScheduleTrainClassAndCoach(
            ScheduleTrainClass scheduleTrainClass,
            Coach coach
    );

    List<ScheduleTrainCoach>
    findByScheduleTrainClass_ScheduleTrainInAndCoachAndIsDeletedFalse(
            List<ScheduleTrain> scheduleTrains,
            Coach coach
    );


    @Query("""
        SELECT s FROM ScheduleTrainCoach s
        WHERE
        (
            (:id IS NOT NULL AND s.id = :id)
            OR (:scheduleTrainClassId IS NOT NULL
                AND s.scheduleTrainClass.id = :scheduleTrainClassId)
            OR (:coachId IS NOT NULL
                AND s.coach.id = :coachId)
            OR (:coachPosition IS NOT NULL
                AND s.coachPosition = :coachPosition)
            OR (:status IS NOT NULL
                AND s.status = :status)
        )
        """)
    List<ScheduleTrainCoach> searchScheduleTrainCoach(
            @Param("id") Long id,
            @Param("scheduleTrainClassId")
            Long scheduleTrainClassId,
            @Param("coachId") Long coachId,
            @Param("coachPosition") String coachPosition,
            @Param("status") GeneralStatus status
    );
}