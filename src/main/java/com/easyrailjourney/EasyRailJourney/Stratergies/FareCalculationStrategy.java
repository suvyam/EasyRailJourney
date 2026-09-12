package com.easyrailjourney.EasyRailJourney.Stratergies;

import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareCalculationReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareCalculationRespDto;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;




public interface FareCalculationStrategy {

    FareStrategyType getStrategyType();

    FareCalculationRespDto calculate(Train train,FareCalculationReqDto request );
}