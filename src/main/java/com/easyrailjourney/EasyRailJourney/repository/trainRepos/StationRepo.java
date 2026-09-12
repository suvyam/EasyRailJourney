package com.easyrailjourney.EasyRailJourney.repository.trainRepos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.Station;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface StationRepo extends JpaRepository<Station, Long> {

    Optional<Station> findByCode(String code);

    Optional<Station> findByName(String name);

    @Query("""
        SELECT s FROM Station s
        WHERE
        (
            (:id IS NOT NULL AND s.id = :id)
            OR (:code IS NOT NULL AND s.code = :code)
            OR (:name IS NOT NULL AND s.name = :name)
        )
        AND s.isDeleted = :isDeleted
        """)
    List<Station> searchStation(
            @Param("id") Long id,
            @Param("code") String code,
            @Param("name") String name,
            @Param("isDeleted") boolean isDeleted
    );
}