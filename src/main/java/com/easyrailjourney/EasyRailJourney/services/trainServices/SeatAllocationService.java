package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Stratergies.SeatAllocationStrategy;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClass;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainClassSeatRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatAllocationService {

    private final ScheduleTrainClassSeatRepo scheduleTrainClassSeatRepo;
    private final SeatAllocationStrategy seatAllocationStrategy;

    public List<ScheduleTrainClassSeat> findSeat(
            ScheduleTrainClass scheduleTrainClass,
            int numberOfSeats,
            Long bookingId) {

        return scheduleTrainClassSeatRepo.findSeat(
                scheduleTrainClass.getId(),
                bookingId,
                numberOfSeats
        );
    }

    public boolean allocateSeat(
            List<ScheduleTrainClassSeat> seats,
            Bookings booking) throws Exception {

        return seatAllocationStrategy.allocateSeat(
                seats,
                booking
        );
    }
}