package com.easyrailjourney.EasyRailJourney.Stratergies;

import java.util.List;

import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;

public interface SeatAllocationStrategy {

    boolean allocateSeat(
            List<ScheduleTrainClassSeat> seats,
            Bookings booking
    ) throws Exception;
}