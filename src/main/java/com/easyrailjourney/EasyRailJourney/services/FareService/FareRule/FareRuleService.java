package com.easyrailjourney.EasyRailJourney.services.FareService.FareRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;

@Service
public class FareRuleService {

    private final FareRuleRepo fareRuleRepo;
    private final TrainRepo trainRepo;
    private StateRepo stateRepo;
    private TrainClassRepo trainClassRepo;

    public FareRuleService(FareRuleRepo fareRuleRepo,TrainRepo trainRepo, StateRepo stateRepo,   TrainClassRepo trainClassRepo) {
        this.fareRuleRepo = fareRuleRepo;
        this.trainRepo = trainRepo;
        this.stateRepo = stateRepo;
        this.trainClassRepo = trainClassRepo;
    }

    // CREATE
    @Transactional 
    public FareRuleRespDto createFareRule(
            FareRuleCreateReqDto reqDto) throws Exception {



        FareRule fareRule = new FareRule();

        /*
         * Set only the fields which are provided
         * in the request DTO.
         */

        // Train
        if (reqDto.getTrainId() != null) {

           Train train = trainRepo.findById(reqDto.getTrainId()).orElseThrow(()-> new Exception("Train not found"));

            fareRule.setTrain(train);
        }

        // State
        if (reqDto.getStateId() != null) {
             
           States  state = stateRepo.findById(reqDto.getStateId()).orElseThrow(()-> new Exception("State not found"));
            fareRule.setState(state);
        }

        // Class Type
        if (reqDto.getClassName() != null) {
            TrainClass trainClass = trainClassRepo.findByClassName(reqDto.getClassName()).orElseThrow(()-> new Exception("ClassName not found"));
            fareRule.setClassType(trainClass);
        }

        // Calculation Type
       // Calculation Type
        if (reqDto.getCalculationType() != null &&
        !reqDto.getCalculationType().isBlank()) {

        try {

        FareCalculationType calculationType =
                FareCalculationType.valueOf(
                        reqDto.getCalculationType().trim().toUpperCase()
                );

        fareRule.setCalculationType(calculationType);

        } catch (IllegalArgumentException e) {

                throw new Exception(
                        "Invalid calculation type. Allowed values: FIXED, PER_KM, PERCENTAGE, MULTIPLIER"
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

        } else {

            // Default value
            fareRule.setActive(true);
        }

        // New rule should never be deleted
        fareRule.setIsDeleted(false);

        
         fareRuleRepo.save(fareRule);

         return wrapper(new ArrayList<>(List.of(fareRule))).get(0);
    }


    // GET ALL
    public List<FareRuleRespDto> getAllFareRules() {

        List<FareRule> list = fareRuleRepo.findAll();

        return wrapper(list);
    }

    public  List<FareRuleRespDto> wrapper(List<FareRule> reqDto){

        List<FareRuleRespDto> list = new ArrayList<>();

        for(FareRule fr : reqDto){

            FareRuleRespDto fareRuleRespDto = new FareRuleRespDto();
            Optional<Train> train = Optional.ofNullable(fr.getTrain());
            if(train.isPresent())fareRuleRespDto.setTrainName(train.get().getTrainName());

            Optional<States> state = Optional.ofNullable(fr.getState());
            if(state.isPresent())fareRuleRespDto.setStateName(state.get().getName());

            fareRuleRespDto.setClassType(fr.getClassType().getClassName());

            fareRuleRespDto.setCalculationType(fr.getCalculationType().toString());

            fareRuleRespDto.setValue(fr.getValue());

            fareRuleRespDto.setPriority(fr.getPriority());

            fareRuleRespDto.setActive(true);

            fareRuleRespDto.setIsDeleted(false);

            list.add(fareRuleRespDto);

        }

        
        return list;


    }


    // SEARCH
    public List<FareRuleRespDto> searchFareRule(
            Long trainId,
            Long stateId,
            Long classType) {


        List<FareRule> list = fareRuleRepo.findApplicableFareRules(trainId, stateId,classType);

        return wrapper(list);
    }


      // UPDATE
      @Transactional 
      public boolean updateFareRule(
        FareRuleUpdateReqDto reqDto) throws Exception {

    FareRule fareRule =
            fareRuleRepo.findById(reqDto.getId())
                    .orElseThrow(() ->
                            new Exception("Fare rule not found"));


    // Train
    if (reqDto.getTrainId() != null) {

        Train train = trainRepo.findById(reqDto.getTrainId())
                .orElseThrow(() ->
                        new Exception("Train not found"));

        fareRule.setTrain(train);
    }


    // State
    if (reqDto.getStateId() != null) {

        States state = stateRepo.findById(reqDto.getStateId())
                .orElseThrow(() ->
                        new Exception("State not found"));

        fareRule.setState(state);
    }


    // Class Type
    if (reqDto.getClassName() != null &&
            !reqDto.getClassName().isBlank()) {

        TrainClass trainClass =
                trainClassRepo.findByClassName(
                        reqDto.getClassName()
                ).orElseThrow(() ->
                        new Exception("ClassName not found"));

        fareRule.setClassType(trainClass);
    }


    // Calculation Type
    if (reqDto.getCalculationType() != null &&
            !reqDto.getCalculationType().isBlank()) {

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
                    "Allowed values: FIXED, PER_KM, PERCENTAGE, MULTIPLIER"
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


    // SOFT DELETE
    @Transactional 
    public boolean deleteFareRule(
            FareRuleDeleteReqDto reqDto) throws Exception {

        FareRule fareRule =
                fareRuleRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception("Fare rule not found"));

        fareRule.setIsDeleted(true);

        fareRuleRepo.save(fareRule);

        return true;
    }


    // PERMANENT DELETE
    @Transactional 
    public boolean deleteFareRulePermanently(
            FareRuleDeleteReqDto reqDto) throws Exception {

        FareRule fareRule =
                fareRuleRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception("Fare rule not found"));

        fareRuleRepo.delete(fareRule);

        return true;
    }
}