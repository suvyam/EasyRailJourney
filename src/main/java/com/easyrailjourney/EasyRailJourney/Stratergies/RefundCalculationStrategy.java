package com.easyrailjourney.EasyRailJourney.Stratergies;

import com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos.RefundCalculationRespDto;
import com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundStrategyType;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;

public interface RefundCalculationStrategy {

    RefundStrategyType getStrategyType();

    RefundCalculationRespDto calculate(Bookings booking);
}