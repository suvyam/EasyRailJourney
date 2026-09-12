package com.easyrailjourney.EasyRailJourney.repository.trainRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrain;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClass;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainClass;




public interface ScheduleTrainClassRepo
        extends JpaRepository<ScheduleTrainClass, Long> {

    Optional<ScheduleTrainClass>
    findByScheduleTrainAndTrainClass(
            ScheduleTrain scheduleTrain,
            TrainClass trainClass
    );


    @Query("""
        SELECT s
        FROM ScheduleTrainClass s
        WHERE
        (
            (:id IS NOT NULL AND s.id = :id)
            OR (:scheduleTrainId IS NOT NULL
                AND s.scheduleTrain.id = :scheduleTrainId)
            OR (:trainClassId IS NOT NULL
                AND s.trainClass.id = :trainClassId)
        )
        """)
    List<ScheduleTrainClass> searchScheduleTrainClass(
            @Param("id") Long id,
            @Param("scheduleTrainId") Long scheduleTrainId,
            @Param("trainClassId") Long trainClassId
    );
}