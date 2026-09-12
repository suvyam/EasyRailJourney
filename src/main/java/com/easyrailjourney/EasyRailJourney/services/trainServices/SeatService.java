package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Seats.SeatCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Seats.SeatDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Seats.SeatUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Coach;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Seat;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.SeatType;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.CoachRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.SeatRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.SeatTypeRepo;


@Service
public class SeatService {

    private final SeatRepo seatRepo;
    private final CoachRepo coachRepo;
    private final SeatTypeRepo seatTypeRepo;

    public SeatService(
            SeatRepo seatRepo,
            CoachRepo coachRepo,
            SeatTypeRepo seatTypeRepo) {

        this.seatRepo = seatRepo;
        this.coachRepo = coachRepo;
        this.seatTypeRepo = seatTypeRepo;
    }

    // CREATE
    public Seat createSeat(
            SeatCreateReqDto reqDto) throws Exception {

        Coach coach = coachRepo.findById(reqDto.getCoachId())
                .orElseThrow(() ->
                        new Exception("Coach not found"));

        SeatType seatType =
                seatTypeRepo.findById(reqDto.getSeatTypeId())
                        .orElseThrow(() ->
                                new Exception("Seat type not found"));

        Optional<Seat> existingSeat =
                seatRepo.findByCoachAndSeatNumber(
                        coach,
                        reqDto.getSeatNumber()
                );

        if (existingSeat.isPresent()) {
            throw new Exception(
                    "Seat already exists in this coach"
            );
        }

        Seat seat = new Seat();

        seat.setCoach(coach);
        seat.setSeatType(seatType);
        seat.setSeatNumber(reqDto.getSeatNumber());
        seat.setDeleted(false);

        return seatRepo.save(seat);
    }

    // GET ALL
    public List<Seat> getAllSeats() {

        return seatRepo.findAll();
    }

    // SEARCH
    public List<Seat> searchSeat(
            Long id,
            Long coachId,
            String seatNumber,
            Long seatTypeId,
            boolean isDeleted) {

        return seatRepo.searchSeat(
                id,
                coachId,
                seatNumber,
                seatTypeId,
                isDeleted
        );
    }

    // UPDATE
    public boolean updateSeat(
            SeatUpdateReqDto reqDto) throws Exception {

        Seat seat =
                seatRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception("Seat not found"));

        if (reqDto.getCoachId() != null) {

            Coach coach =
                    coachRepo.findById(reqDto.getCoachId())
                            .orElseThrow(() ->
                                    new Exception(
                                            "Coach not found"
                                    ));

            seat.setCoach(coach);
        }

        if (reqDto.getSeatTypeId() != null) {

            SeatType seatType =
                    seatTypeRepo.findById(reqDto.getSeatTypeId())
                            .orElseThrow(() ->
                                    new Exception(
                                            "Seat type not found"
                                    ));

            seat.setSeatType(seatType);
        }

        if (reqDto.getSeatNumber() != null) {
            seat.setSeatNumber(reqDto.getSeatNumber());
        }

        seatRepo.save(seat);

        return true;
    }

    // SOFT DELETE
    public boolean deleteSeat(
            SeatDeleteReqDto reqDto) throws Exception {

        Seat seat = findSeatForDelete(reqDto);

        seat.setDeleted(true);

        seatRepo.save(seat);

        return true;
    }

    // PERMANENT DELETE
    public boolean deleteSeatPermanently(
            SeatDeleteReqDto reqDto) throws Exception {

        Seat seat = findSeatForDelete(reqDto);

        seatRepo.delete(seat);

        return true;
    }

    private Seat findSeatForDelete(
            SeatDeleteReqDto reqDto) throws Exception {

        if (reqDto.getId() != null) {

            return seatRepo.findById(reqDto.getId())
                    .orElseThrow(() ->
                            new Exception("Seat not found"));
        }

        if (reqDto.getCoachId() != null &&
                reqDto.getSeatNumber() != null) {

            Coach coach =
                    coachRepo.findById(reqDto.getCoachId())
                            .orElseThrow(() ->
                                    new Exception(
                                            "Coach not found"
                                    ));

            return seatRepo
                    .findByCoachAndSeatNumber(
                            coach,
                            reqDto.getSeatNumber()
                    )
                    .orElseThrow(() ->
                            new Exception("Seat not found"));
        }

        throw new Exception(
                "Provide seat id or coach id with seat number"
        );
    }
}