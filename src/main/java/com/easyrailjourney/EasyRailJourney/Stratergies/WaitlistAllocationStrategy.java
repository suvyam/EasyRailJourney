package com.easyrailjourney.EasyRailJourney.Stratergies;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;

public interface WaitlistAllocationStrategy {

    boolean allocateNextPassenger(
            ScheduleTrainClassSeat seat
    );
}