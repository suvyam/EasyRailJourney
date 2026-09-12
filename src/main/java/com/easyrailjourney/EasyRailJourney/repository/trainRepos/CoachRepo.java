package com.easyrailjourney.EasyRailJourney.repository.trainRepos;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.Coach;

public interface CoachRepo extends JpaRepository<Coach, Long> {

    Optional<Coach> findByCoachNumber(String coachNumber);

    @Query("""
        SELECT c FROM Coach c
        WHERE
        (
            (:id IS NOT NULL AND c.id = :id)
            OR (:coachNumber IS NOT NULL AND c.coachNumber = :coachNumber)
            OR (:coachTypeId IS NOT NULL AND c.coachType.id = :coachTypeId)
        )
        AND c.isDeleted = :isDeleted
        """)
    List<Coach> searchCoach(
            @Param("id") Long id,
            @Param("coachNumber") String coachNumber,
            @Param("coachTypeId") Long coachTypeId,
            @Param("isDeleted") boolean isDeleted
    );

    @Modifying
@Query("""
       UPDATE Seat s
       SET s.coach.id = :coachId
       WHERE s.id IN :seatIds
       """)
int assignSeatsToCoach(
        @Param("coachId") Long coachId,
        @Param("seatIds") List<Long> seatIds
);
}