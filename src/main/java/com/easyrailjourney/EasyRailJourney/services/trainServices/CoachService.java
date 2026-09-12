package com.easyrailjourney.EasyRailJourney.services.trainServices;


import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Coach;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.CoachType;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.CoachRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.CoachTypeRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.SeatRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.TrainClassRepo;




@Service
public class CoachService {

    private final CoachRepo coachRepo;
    private final TrainClassRepo trainClassRepo;
    private final CoachTypeRepo coachTypeRepo;
    private final SeatRepo seatRepo;



    public CoachService(
            CoachRepo coachRepo,
            TrainClassRepo trainClassRepo,
            CoachTypeRepo coachTypeRepo,
            SeatRepo seatRepo) {

        this.coachRepo = coachRepo;
        this.trainClassRepo = trainClassRepo;
        this.coachTypeRepo = coachTypeRepo;
        this.seatRepo = seatRepo;
    }

    // CREATE
    public Coach createCoach(
            CoachCreateReqDto reqDto) throws Exception {

        if (reqDto.getCoachNumber() == null ||
                reqDto.getCoachNumber().isBlank()) {

            throw new Exception("Coach number is required");
        }


        if (coachRepo.findByCoachNumber(
                reqDto.getCoachNumber()).isPresent()) {

            throw new Exception(
                    "Coach number already exists"
            );
        }

        CoachType coachType =
            coachTypeRepo.findById(reqDto.getCoachTypeId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "CoachType not found with id: "
                                    + reqDto.getCoachTypeId()
                            ));

        Coach coach = new Coach();

        coach.setCoachNumber(reqDto.getCoachNumber());
        coach.setDeleted(false);
        coach.setCoachType(coachType);
        return coachRepo.save(coach);
    }

    // GET ALL
    public List<Coach> getAllCoaches() {

        return  coachRepo.findAll();
    }

    // SEARCH
    public List<Coach> searchCoach(
            Long id,
            String coachNumber,
            Long coachTypeId,
            boolean isDeleted) {

        return coachRepo.searchCoach(
                id,
                coachNumber,
                coachTypeId,
                isDeleted
        );
    }

    // UPDATE
    public boolean updateCoach(
            CoachUpdateReqDto reqDto) throws Exception {

        Coach coach =
                coachRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception("Coach not found"));

        if (reqDto.getCoachNumber() != null) {

            coach.setCoachNumber(
                    reqDto.getCoachNumber()
            );
        }


        coachRepo.save(coach);

        return true;
    }

    // SOFT DELETE
    public boolean deleteCoach(
            CoachDeleteReqDto reqDto) throws Exception {

        Coach coach = findCoachForDelete(reqDto);

        coach.setDeleted(true);

        coachRepo.save(coach);

        return true;
    }

    // PERMANENT DELETE
    public boolean deleteCoachPermanently(
            CoachDeleteReqDto reqDto) throws Exception {

        Coach coach = findCoachForDelete(reqDto);

        coachRepo.delete(coach);

        return true;
    }

    private Coach findCoachForDelete(
            CoachDeleteReqDto reqDto) throws Exception {

        if (reqDto.getId() != null) {

            return coachRepo.findById(reqDto.getId())
                    .orElseThrow(() ->
                            new Exception("Coach not found"));
        }

        if (reqDto.getCoachNumber() != null) {

            return coachRepo.findByCoachNumber(
                            reqDto.getCoachNumber()
                    )
                    .orElseThrow(() ->
                            new Exception("Coach not found"));
        }

        throw new Exception(
                "Provide coach id or coach number"
        );
    }

    
}