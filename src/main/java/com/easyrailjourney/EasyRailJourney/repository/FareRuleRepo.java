package com.easyrailjourney.EasyRailJourney.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.models.FareRule;


public interface FareRuleRepo extends JpaRepository<FareRule, Long> {

    List<FareRule> findByClassTypeAndActiveTrueOrderByPriorityDesc(
            Object classType
    );

    @Query("""
    SELECT f
    FROM FareRule f
    WHERE f.isDeleted = false
      AND f.active = true
      AND f.classType.id = :classType

      AND (
            f.train IS NULL
            OR f.train.id = :trainId
          )

      AND (
            f.state IS NULL
            OR f.state.id = :stateId
          )

    ORDER BY
        CASE
            WHEN f.train IS NOT NULL AND f.state IS NOT NULL THEN 1
            WHEN f.train IS NOT NULL AND f.state IS NULL THEN 2
            WHEN f.train IS NULL AND f.state IS NOT NULL THEN 3
            ELSE 4
        END ASC,

        f.priority ASC
""")
List<FareRule> findApplicableFareRules(
        @Param("trainId") Long trainId,
        @Param("stateId") Long stateId,
        @Param("classType") Long classType
);
}