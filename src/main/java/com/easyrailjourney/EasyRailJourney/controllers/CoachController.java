package com.easyrailjourney.EasyRailJourney.controllers;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.GeneralCoachRespDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Coach;
import com.easyrailjourney.EasyRailJourney.services.trainServices.CoachService;


@RestController
@RequestMapping("/coach")
public class CoachController {

    private final CoachService coachService;

    CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    // CREATE COACH
    @PostMapping
    public ResponseEntity<GeneralCoachRespDto> createCoach(
            @RequestBody CoachCreateReqDto reqDto) {

        GeneralCoachRespDto respDto =
                new GeneralCoachRespDto();

        try {

            Coach coach =
                    coachService.createCoach(reqDto);

            respDto.setCoach(coach);
            respDto.setResponseStatus(
                    ResponseStatus.SUCCESS
            );

        } catch (Exception e) {

            respDto.setResponseStatus(
                    ResponseStatus.FAILURE
            );

            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<GeneralCoachRespDto> getAllCoaches() {

        GeneralCoachRespDto respDto =
                new GeneralCoachRespDto();

        try {

            List<Coach> coaches =
                    coachService.getAllCoaches();

            respDto.setCoaches(coaches);
            respDto.setResponseStatus(
                    ResponseStatus.SUCCESS
            );

        } catch (Exception e) {

            respDto.setResponseStatus(
                    ResponseStatus.FAILURE
            );

            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // SEARCH
    @GetMapping("/search")
    public ResponseEntity<GeneralCoachRespDto> searchCoach(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String coachNumber,
            @RequestParam(required = false) Long coachTypeId,
            @RequestParam(defaultValue = "false")
            boolean isDeleted) {

        GeneralCoachRespDto respDto =
                new GeneralCoachRespDto();

        if (id == null &&
                coachNumber == null &&
                coachTypeId == null) {

            return ResponseEntity.notFound().build();
        }

        try {

            List<Coach> coaches =
                    coachService.searchCoach(
                            id,
                            coachNumber,
                            coachTypeId,
                            isDeleted
                    );

            respDto.setCoaches(coaches);
            respDto.setResponseStatus(
                    ResponseStatus.SUCCESS
            );

        } catch (Exception e) {

            respDto.setResponseStatus(
                    ResponseStatus.FAILURE
            );

            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<GeneralCoachRespDto> updateCoach(
            @RequestBody CoachUpdateReqDto reqDto) {

        GeneralCoachRespDto respDto =
                new GeneralCoachRespDto();

        try {

            boolean ans =
                    coachService.updateCoach(reqDto);

            if (ans) {

                respDto.setMessage(
                        "Successfully Updated"
                );

                respDto.setResponseStatus(
                        ResponseStatus.SUCCESS
                );

            } else {

                respDto.setMessage("Not Updated");

                respDto.setResponseStatus(
                        ResponseStatus.FAILURE
                );
            }

        } catch (Exception e) {

            respDto.setMessage("Failed Updated");

            respDto.setResponseStatus(
                    ResponseStatus.FAILURE
            );
        }

        return ResponseEntity.badRequest().body(respDto);
    }

    // SOFT DELETE
    @DeleteMapping
    public ResponseEntity<GeneralCoachRespDto> deleteCoach(
            @RequestBody CoachDeleteReqDto reqDto) {

        GeneralCoachRespDto respDto =
                new GeneralCoachRespDto();

        try {

            boolean ans =
                    coachService.deleteCoach(reqDto);

            if (!ans) {

                respDto.setMessage("Not Deleted");

                respDto.setResponseStatus(
                        ResponseStatus.FAILURE
                );

            } else {

                respDto.setMessage(
                        "Deleted Successfully"
                );

                respDto.setResponseStatus(
                        ResponseStatus.SUCCESS
                );
            }

        } catch (Exception e) {

            respDto.setMessage(
                    "Delete Interruption " +
                    e.getMessage()
            );

            respDto.setResponseStatus(
                    ResponseStatus.FAILURE
            );
        }

        return ResponseEntity.ok().body(respDto);
    }

    // PERMANENT DELETE
    @DeleteMapping("/delete-permanently")
    public ResponseEntity<GeneralCoachRespDto>
            deleteCoachPermanently(
                    @RequestBody CoachDeleteReqDto reqDto) {

        GeneralCoachRespDto respDto =
                new GeneralCoachRespDto();

        try {

            boolean ans =
                    coachService.deleteCoachPermanently(reqDto);

            if (!ans) {

                respDto.setMessage("Not Deleted");

                respDto.setResponseStatus(
                        ResponseStatus.FAILURE
                );

            } else {

                respDto.setMessage(
                        "Deleted Successfully"
                );

                respDto.setResponseStatus(
                        ResponseStatus.SUCCESS
                );
            }

        } catch (Exception e) {

            respDto.setMessage(
                    "Delete Interruption " +
                    e.getMessage()
            );

            respDto.setResponseStatus(
                    ResponseStatus.FAILURE
            );
        }

        return ResponseEntity.ok().body(respDto);
    }

    
}