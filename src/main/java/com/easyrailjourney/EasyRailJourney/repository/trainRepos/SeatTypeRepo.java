package com.easyrailjourney.EasyRailJourney.repository.trainRepos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.SeatType;

public interface SeatTypeRepo
        extends JpaRepository<SeatType, Long> {

    boolean existsByTypeCode(String typeCode);


    @Query("""
            SELECT s
            FROM SeatType s
            WHERE (:id IS NULL OR s.id = :id)
            AND (:typeCode IS NULL OR s.typeCode = :typeCode)
            AND (:typeName IS NULL OR s.typeName = :typeName)
            AND s.isDeleted = :isDeleted
            """)
    List<SeatType> searchSeatType(
            @Param("id") Long id,
            @Param("typeCode") String typeCode,
            @Param("typeName") String typeName,
            @Param("isDeleted") boolean isDeleted
    );
}