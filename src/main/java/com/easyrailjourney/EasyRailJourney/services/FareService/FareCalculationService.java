package com.easyrailjourney.EasyRailJourney.services.FareService;


import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareCalculationReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareCalculationRespDto;
import com.easyrailjourney.EasyRailJourney.Stratergies.FareCalculationStrategy;
import com.easyrailjourney.EasyRailJourney.Stratergies.FareStrategyType;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.TrainRepo;

@Service
public class FareCalculationService {

    private final TrainRepo trainRepo;

    private final Map<FareStrategyType, FareCalculationStrategy>
            strategyMap;

    public FareCalculationService(
            TrainRepo trainRepo,
            List<FareCalculationStrategy> strategies
    ) {

        this.trainRepo = trainRepo;

        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        FareCalculationStrategy::getStrategyType,
                        Function.identity()
                ));
    }

    public FareCalculationRespDto calculate(
            FareCalculationReqDto request
    ) {

        Train train = trainRepo.findById(
                request.getTrainId()
        ).orElseThrow(() ->
                new RuntimeException("Train not found")
        );

        if (train.getIsDeleted()) {
            throw new RuntimeException("Train is deleted");
        }

        if (!train.getStatus()) {
            throw new RuntimeException("Train is inactive");
        }

        FareStrategyType strategyType =
                train.getFareStrategyType();

        FareCalculationStrategy strategy =
                strategyMap.get(strategyType);

        if (strategy == null) {
            throw new RuntimeException(
                    "No fare strategy configured for: "
                            + strategyType
            );
        }

        return strategy.calculate(
                train,
                request
        );
    }
}