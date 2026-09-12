package com.easyrailjourney.EasyRailJourney.services.trainServices;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachSeatDtos.AssignCoachSeatReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachSeatDtos.CoachSeatRespDto;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Coach;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Seat;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.CoachRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.SeatRepo;


@Service 

public class CoachSeatService{

    private final CoachRepo coachRepo;
    private final SeatRepo seatRepo;



    public CoachSeatService(
            CoachRepo coachRepo,
            SeatRepo seatRepo) {

        this.coachRepo = coachRepo;
        this.seatRepo = seatRepo;
    }

    @Transactional
    public CoachSeatRespDto assignSeats(AssignCoachSeatReqDto dto) {
    
         Coach coach = coachRepo.findById(dto.getCoachId())
                 .orElseThrow(() ->
                         new RuntimeException("Coach not found with ID: "
                                 + dto.getCoachId()));
    
         List<Seat> seats = seatRepo.findAllById(dto.getSeatIds());
    
         if (seats.size() != dto.getSeatIds().size()) {
                 throw new RuntimeException("One or more seats not found");
         }
    
         for (Seat seat : seats) {
    
                 if (seat.getCoach() != null) {
                 throw new RuntimeException(
                         "Seat " + seat.getId()
                                 + " is already assigned to a coach"
                 );
                 }
    
                 seat.setCoach(coach);
         }
    
         seatRepo.saveAll(seats);
    
         return new CoachSeatRespDto(
                 coach.getId(),
                 dto.getSeatIds(),
                 "Seats assigned to coach successfully"
         );
     }
    
    
    
    
    
     @Transactional(readOnly = true)
    public CoachSeatRespDto getSeatsByCoachId(Long coachId) {
    
     Coach coach = coachRepo.findById(coachId)
             .orElseThrow(() ->
                     new RuntimeException(
                             "Coach not found with ID: " + coachId));
    
     if (coach.getSeats() == null) {
         throw new RuntimeException(
                 "Seat is not assigned to any coach");
     }
    
     return new CoachSeatRespDto(
        coach.getId(),
        coach.getSeats()
                .stream()
                .map(a -> a.getId())
                .toList(),
        "Coach fetched successfully"

     );
    }    
    
    
    
    
    
    @Transactional
         public CoachSeatRespDto updateSeats(
                 Long coachId,
                 AssignCoachSeatReqDto dto) {
    
         Coach coach = coachRepo.findById(coachId)
                 .orElseThrow(() ->
                         new RuntimeException(
                                 "Coach not found with ID: " + coachId));
    
         if (!coachId.equals(dto.getCoachId())) {
                 throw new RuntimeException(
                         "Coach ID in path and request body must match");
         }
    
         List<Seat> newSeats =
                 seatRepo.findAllById(dto.getSeatIds());
    
         if (newSeats.size() != dto.getSeatIds().size()) {
                 throw new RuntimeException(
                         "One or more seats not found");
         }
    
         // Remove old assignments
         List<Seat> oldSeats =
                 seatRepo.findByCoach_id(coachId);
    
         for (Seat seat : oldSeats) {
                 seat.setCoach(null);
         }
    
         seatRepo.saveAll(oldSeats);
    
         // Assign new seats
         for (Seat seat : newSeats) {
    
                 if (seat.getCoach() != null
                         && !seat.getCoach().getId().equals(coachId)) {
    
                 throw new RuntimeException(
                         "Seat " + seat.getId()
                                 + " already belongs to another coach");
                 }
    
                 seat.setCoach(coach);
         }
    
         seatRepo.saveAll(newSeats);
    
         return new CoachSeatRespDto(
                 coachId,
                 dto.getSeatIds(),
                 "Coach seats updated successfully"
         );
    }
    
    
    
    
    @Transactional
    public String removeSeatFromCoach(
         Long coachId,
         Long seatId) {
    
     Coach coach = coachRepo.findById(coachId)
             .orElseThrow(() ->
                     new RuntimeException(
                             "Coach not found with ID: " + coachId));
    
     Seat seat = seatRepo.findById(seatId)
             .orElseThrow(() ->
                     new RuntimeException(
                             "Seat not found with ID: " + seatId));
    
     if (seat.getCoach() == null) {
         throw new RuntimeException(
                 "Seat is not assigned to any coach");
     }
    
     if (!seat.getCoach().getId().equals(coach.getId())) {
         throw new RuntimeException(
                 "Seat does not belong to this coach");
     }
    
     seat.setCoach(null);
    
     seatRepo.save(seat);
    
     return "Seat removed from coach successfully";
    }
    
    @Transactional(readOnly = true)
    public CoachSeatRespDto getCoachBySeatId(Long seatId) {
    
     Seat seat = seatRepo.findById(seatId)
             .orElseThrow(() ->
                     new RuntimeException(
                             "Seat not found with ID: " + seatId));
    
     Coach coach = seat.getCoach();
    
     if (coach == null) {
         throw new RuntimeException(
                 "Seat with ID " + seatId
                         + " is not assigned to any coach");
     }
    
     return new CoachSeatRespDto(
             coach.getId(),
             List.of(seat.getId()),
             "Coach fetched successfully"
     );
    }
    
}

