package com.easyrailjourney.EasyRailJourney.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Stratergies.WaitlistAllocationStrategy;
import com.easyrailjourney.EasyRailJourney.models.SeatWaitlist;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;
import com.easyrailjourney.EasyRailJourney.repository.SeatWaitlistRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WaitlistService {

    private final SeatWaitlistRepo seatWaitlistRepo;
    private final WaitlistAllocationStrategy waitlistAllocationStrategy;

    public Optional<SeatWaitlist> findNextPassenger(
            ScheduleTrainClassSeat seat) {

        return seatWaitlistRepo
                .findFirstBySeatOrderByPriorityAscPositionAscCreatedAtAsc(
                        seat
                );
    }

    public Integer getNextPosition(
            ScheduleTrainClassSeat seat) {

        Integer lastPosition =
                seatWaitlistRepo.findLastPosition(seat);

        return lastPosition + 1;
    }

    @Transactional
    public boolean allocateNextPassenger(
            ScheduleTrainClassSeat seat) {

        return waitlistAllocationStrategy
                .allocateNextPassenger(seat);
    }

    @Transactional
    public void removeFromWaitlist (
            Long waitlistId) throws Exception{

                Optional<SeatWaitlist> waitlist = seatWaitlistRepo.findById(waitlistId);
                if(waitlist.isEmpty())throw new Exception("Wait list id not found");

        seatWaitlistRepo.delete(waitlist.get());
    }
}