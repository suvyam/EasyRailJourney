package com.easyrailjourney.EasyRailJourney.repository.trainRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainClass;



public interface TrainClassRepo extends JpaRepository<TrainClass, Long> {

    boolean existsByClassCode(String classCode);

    boolean existsByClassName(String className);

    Optional<TrainClass> findByClassName(String className);

    Optional<TrainClass> findByClassCode(String classCode);

    @Query("""
        SELECT tc FROM TrainClass tc
        WHERE
        (
            (:id IS NOT NULL AND tc.id = :id)
            OR (:classCode IS NOT NULL AND tc.classCode = :classCode)
            OR (:className IS NOT NULL AND tc.className = :className)
        )
        AND tc.isDeleted = :isDeleted
        """)
    List<TrainClass> searchTrainClass(
            @Param("id") Long id,
            @Param("classCode") String classCode,
            @Param("className") String className,
            @Param("isDeleted") boolean isDeleted
    );
}