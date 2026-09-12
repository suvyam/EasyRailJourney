package com.easyrailjourney.EasyRailJourney.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.models.RefundRule;

public interface RefundRuleRepo
        extends JpaRepository<RefundRule, Long> {

    List<RefundRule> findByActiveTrueOrderByPriorityDesc();

     @Query("""
        SELECT r
        FROM RefundRule r
        WHERE r.train.id = :trainId
          AND r.active = true
          AND r.cancellationHours <= :hoursBeforeJourney
        ORDER BY r.cancellationHours DESC, r.priority DESC
    """)
    List<RefundRule> findApplicableRules(
            @Param("trainId") Long trainId,
            @Param("hoursBeforeJourney") Long hoursBeforeJourney
    );
}