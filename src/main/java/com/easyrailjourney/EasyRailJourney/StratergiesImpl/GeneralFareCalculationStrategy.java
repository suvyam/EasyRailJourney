package com.easyrailjourney.EasyRailJourney.StratergiesImpl;


import java.util.List;

import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareCalculationReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareCalculationRespDto;
import com.easyrailjourney.EasyRailJourney.Stratergies.FareCalculationStrategy;
import com.easyrailjourney.EasyRailJourney.Stratergies.FareCalculationType;
import com.easyrailjourney.EasyRailJourney.Stratergies.FareStrategyType;
import com.easyrailjourney.EasyRailJourney.models.FareRule;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Station;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;
import com.easyrailjourney.EasyRailJourney.repository.FareRuleRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.StationRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 

public class GeneralFareCalculationStrategy implements FareCalculationStrategy {

    private final FareRuleRepo fareRuleRepo;
    private final StationRepo stationRepo;

    @Override
    public FareStrategyType getStrategyType() {
        return FareStrategyType.GENERAL;
    }


    @Override
    public FareCalculationRespDto calculate(
            Train train,
            FareCalculationReqDto request
    ) {

        Station sourceStation = stationRepo.findById(
                request.getSourceStationId()
        ).orElseThrow(() ->
                new RuntimeException("Source station not found")
        );

        List<FareRule> rules =
                fareRuleRepo.findByClassTypeAndActiveTrueOrderByPriorityDesc(
                        request.getClassType()
                );

        FareRule applicableRule = findApplicableRule(
                rules,
                train,
                sourceStation
        );

        if (applicableRule == null) {
            throw new RuntimeException(
                    "No fare rule found for this train/class/state"
            );
        }

        Double farePerPassenger =
                calculateFare(applicableRule);

        Double totalFare = farePerPassenger * request.getPassengerCount();
        

        FareCalculationRespDto response =
                new FareCalculationRespDto();

        response.setTrainId(train.getId());
        response.setTrainNumber(train.getTrainNumber());
        response.setTrainName(train.getTrainName());

        response.setClassType(
                request.getClassType().toString()
        );

        response.setStrategy(
                train.getFareStrategyType().name()
        );

        response.setAppliedRule(
                buildRuleDescription(
                        applicableRule,
                        sourceStation
                )
        );

        response.setFarePerPassenger(farePerPassenger);

        response.setPassengerCount(
                request.getPassengerCount()
        );

        response.setTotalFare(totalFare);

        return response;
    }

    private FareRule findApplicableRule(
            List<FareRule> rules,
            Train train,
            Station sourceStation
    ) {

        for (FareRule rule : rules) {

            boolean trainMatches =
                    rule.getTrain() == null
                    || rule.getTrain().getId().equals(train.getId());

            boolean stateMatches =
                    rule.getState() == null
                    || rule.getState().getId()
                    .equals(sourceStation.getCity().getId());

            if (trainMatches && stateMatches) {
                return rule;
            }
        }

        return null;
    }

    private Double calculateFare(FareRule rule) {

        if (rule.getCalculationType() == FareCalculationType.FIXED) {

                return Math.round(rule.getValue() * 100.0) / 100.0;
        }

        throw new RuntimeException(
                "Calculation type not implemented yet: "
                        + rule.getCalculationType()
        );
    }

    private String buildRuleDescription(
            FareRule rule,
            Station sourceStation
    ) {

        StringBuilder sb = new StringBuilder();

        if (rule.getTrain() != null) {
            sb.append("TRAIN + ");
        }

        if (rule.getState() != null) {
            sb.append(
                    rule.getState().getName()
            );
        } else {
            sb.append("DEFAULT");
        }

        sb.append(" + ");
        sb.append(rule.getClassType());

        return sb.toString();
    }



}