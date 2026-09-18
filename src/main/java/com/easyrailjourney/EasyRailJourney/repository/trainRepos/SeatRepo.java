package com.easyrailjourney.EasyRailJourney.repository.trainRepos;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.Coach;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Seat;

public interface SeatRepo extends JpaRepository<Seat, Long> {


    List<Seat> findByCoach_IdAndIsDeletedFalse(Long coachId);

    

    Optional<Seat> findByCoachAndSeatNumber(
            Coach coach,
            String seatNumber
    );

    @Query("""
        SELECT s FROM Seat s
        WHERE
        (
            (:id IS NOT NULL AND s.id = :id)
            OR (:coachId IS NOT NULL AND s.coach.id = :coachId)
            OR (:seatNumber IS NOT NULL AND s.seatNumber = :seatNumber)
            OR (:seatTypeId IS NOT NULL AND s.seatType.id = :seatTypeId)
        )
        AND s.isDeleted = :isDeleted
        """)
    List<Seat> searchSeat(
            @Param("id") Long id,
            @Param("coachId") Long coachId,
            @Param("seatNumber") String seatNumber,
            @Param("seatTypeId") Long seatTypeId,
            @Param("isDeleted") boolean isDeleted
    );

    List<Seat> findByCoach_id(Long coachId);
}