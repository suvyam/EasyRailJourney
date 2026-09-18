package com.easyrailjourney.EasyRailJourney.services.FareService.FareRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareRuleCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareRuleDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareRuleRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareRuleUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.Stratergies.FareCalculationType;
import com.easyrailjourney.EasyRailJourney.models.FareRule;
import com.easyrailjourney.EasyRailJourney.models.States;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainClass;
import com.easyrailjourney.EasyRailJourney.repository.FareRuleRepo;
import com.easyrailjourney.EasyRailJourney.repository.StateRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.TrainClassRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.TrainRepo;

@Service
public class FareRuleService {

    private final FareRuleRepo fareRuleRepo;
    private final TrainRepo trainRepo;
    private final StateRepo stateRepo;
    private final TrainClassRepo trainClassRepo;

    public FareRuleService(
            FareRuleRepo fareRuleRepo,
            TrainRepo trainRepo,
            StateRepo stateRepo,
            TrainClassRepo trainClassRepo
    ) {
        this.fareRuleRepo = fareRuleRepo;
        this.trainRepo = trainRepo;
        this.stateRepo = stateRepo;
        this.trainClassRepo = trainClassRepo;
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Transactional(rollbackFor = Exception.class)
    public FareRuleRespDto createFareRule(
            FareRuleCreateReqDto reqDto
    ) throws Exception {

        FareRule fareRule = new FareRule();

        // Train
        if (reqDto.getTrainId() != null) {

            Train train = trainRepo.findById(reqDto.getTrainId())
                    .orElseThrow(() ->
                            new Exception("Train not found")
                    );

            fareRule.setTrain(train);
        }

        // State
        if (reqDto.getStateId() != null) {

            States state = stateRepo.findById(reqDto.getStateId())
                    .orElseThrow(() ->
                            new Exception("State not found")
                    );

            fareRule.setState(state);
        }

        // Class Type
        if (reqDto.getClassName() != null
                && !reqDto.getClassName().isBlank()) {

            TrainClass trainClass =
                    trainClassRepo.findByClassName(
                            reqDto.getClassName()
                    ).orElseThrow(() ->
                            new Exception("ClassName not found")
                    );

            fareRule.setClassType(trainClass);

        } else {

            throw new Exception("Class name is required");
        }

        // Calculation Type
        if (reqDto.getCalculationType() != null
                && !reqDto.getCalculationType().isBlank()) {

            try {

                FareCalculationType calculationType =
                        FareCalculationType.valueOf(
                                reqDto.getCalculationType()
                                        .trim()
                                        .toUpperCase()
                        );

                fareRule.setCalculationType(calculationType);

            } catch (IllegalArgumentException e) {

                throw new Exception(
                        "Invalid calculation type. " +
                        "Allowed values: FIXED, PER_KM, " +
                        "PERCENTAGE, MULTIPLIER"
                );
            }

        } else {

            throw new Exception("Calculation type is required");
        }

        // Value
        if (reqDto.getValue() != null) {
            fareRule.setValue(reqDto.getValue());
        }

        // Priority
        if (reqDto.getPriority() != null) {
            fareRule.setPriority(reqDto.getPriority());
        }


        fareRule.setActive(reqDto.getActive());
  

        fareRule.setBaseFare(reqDto.getBaseFare());



        // New rule is never deleted
        fareRule.setIsDeleted(false);

        fareRuleRepo.save(fareRule);

        return wrapper(
                new ArrayList<>(List.of(fareRule))
        ).get(0);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    public List<FareRuleRespDto> getAllFareRules() {

        List<FareRule> list = fareRuleRepo.findAll();

        return wrapper(list);
    }


    // =========================================================
    // WRAPPER
    // =========================================================

    public List<FareRuleRespDto> wrapper(
            List<FareRule> reqDto
    ) {

        List<FareRuleRespDto> list = new ArrayList<>();

        for (FareRule fr : reqDto) {

            FareRuleRespDto response =
                    new FareRuleRespDto();

            // Train
            Optional<Train> train =
                    Optional.ofNullable(fr.getTrain());

            if (train.isPresent()) {
                response.setTrainName(
                        train.get().getTrainName()
                );
            }

            // State
            Optional<States> state =
                    Optional.ofNullable(fr.getState());

            if (state.isPresent()) {
                response.setStateName(
                        state.get().getName()
                );
            }

            // Class
            if (fr.getClassType() != null) {
                response.setClassType(
                        fr.getClassType().getClassName()
                );
            }

            // Calculation type
            if (fr.getCalculationType() != null) {
                response.setCalculationType(
                        fr.getCalculationType().toString()
                );
            }

            response.setValue(fr.getValue());
            response.setPriority(fr.getPriority());
            response.setActive(fr.isActive());
            response.setIsDeleted(fr.getIsDeleted());

            list.add(response);
        }

        return list;
    }


    // =========================================================
    // SEARCH / APPLICABLE RULES
    // =========================================================

    public List<FareRuleRespDto> searchFareRule(
            Long trainId,
            Long stateId,
            Long classType
    ) {

        List<FareRule> list =
                fareRuleRepo.findApplicableFareRules(
                        trainId,
                        stateId,
                        classType
                );

        return wrapper(list);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Transactional(rollbackFor = Exception.class)
    public boolean updateFareRule(
            FareRuleUpdateReqDto reqDto
    ) throws Exception {

        FareRule fareRule =
                fareRuleRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception("Fare rule not found")
                        );

        // Train
        if (reqDto.getTrainId() != null) {

            Train train =
                    trainRepo.findById(reqDto.getTrainId())
                            .orElseThrow(() ->
                                    new Exception("Train not found")
                            );

            fareRule.setTrain(train);
        }

        // State
        if (reqDto.getStateId() != null) {

            States state =
                    stateRepo.findById(reqDto.getStateId())
                            .orElseThrow(() ->
                                    new Exception("State not found")
                            );

            fareRule.setState(state);
        }

        // Class
        if (reqDto.getClassName() != null
                && !reqDto.getClassName().isBlank()) {

            TrainClass trainClass =
                    trainClassRepo.findByClassName(
                            reqDto.getClassName()
                    ).orElseThrow(() ->
                            new Exception("ClassName not found")
                    );

            fareRule.setClassType(trainClass);
        }

        // Calculation type
        if (reqDto.getCalculationType() != null
                && !reqDto.getCalculationType().isBlank()) {

            try {

                FareCalculationType calculationType =
                        FareCalculationType.valueOf(
                                reqDto.getCalculationType()
                                        .trim()
                                        .toUpperCase()
                        );

                fareRule.setCalculationType(calculationType);

            } catch (IllegalArgumentException e) {

                throw new Exception(
                        "Invalid calculation type. " +
                        "Allowed values: FIXED, PER_KM, " +
                        "PERCENTAGE, MULTIPLIER"
                );
            }
        }

        // Value
        if (reqDto.getValue() != null) {
            fareRule.setValue(reqDto.getValue());
        }

        // Priority
        if (reqDto.getPriority() != null) {
            fareRule.setPriority(reqDto.getPriority());
        }

        // Active
        if (reqDto.getActive() != null) {
            fareRule.setActive(reqDto.getActive());
        }

        fareRuleRepo.save(fareRule);

        return true;
    }


    // =========================================================
    // SOFT DELETE
    // =========================================================

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFareRule(
            FareRuleDeleteReqDto reqDto
    ) throws Exception {

        FareRule fareRule =
                fareRuleRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception("Fare rule not found")
                        );

        fareRule.setIsDeleted(true);

        fareRuleRepo.save(fareRule);

        return true;
    }


    // =========================================================
    // PERMANENT DELETE
    // =========================================================

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFareRulePermanently(
            FareRuleDeleteReqDto reqDto
    ) throws Exception {

        FareRule fareRule =
                fareRuleRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception("Fare rule not found")
                        );

        fareRuleRepo.delete(fareRule);

        return true;
    }
}