package com.easyrailjourney.EasyRailJourney.repository.trainRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainType;

public interface TrainRepo extends JpaRepository<Train, Long> {

    Optional<Train> findByIdAndIsDeleted(
            Long id,
            boolean isDeleted
    );

    Optional<Train> findByTrainNumberAndIsDeleted(
            String trainNumber,
            boolean isDeleted
    );

    @Query("""
        SELECT t FROM Train t
        WHERE (
            (:id IS NOT NULL AND t.id = :id)
            OR (:trainNumber IS NOT NULL AND t.trainNumber = :trainNumber)
            OR (
                :trainName IS NOT NULL
                AND LOWER(t.trainName) LIKE LOWER(CONCAT('%', :trainName, '%'))
            )
            OR (:trainType IS NOT NULL AND t.trainType = :trainType)    
        )
        AND t.isDeleted = :isDeleted
    """)
    List<Train> searchTrain(
            @Param("id") Long id,
            @Param("trainNumber") String trainNumber,
            @Param("trainName") String trainName,
            @Param("trainType") TrainType trainType,
            @Param("isDeleted") boolean isDeleted
    );
}