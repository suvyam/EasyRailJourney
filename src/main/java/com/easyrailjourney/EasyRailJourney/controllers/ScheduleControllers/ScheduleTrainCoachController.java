package com.easyrailjourney.EasyRailJourney.controllers.ScheduleControllers;

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

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCoach.GeneralScheduleTrainCoachRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCoach.ScheduleTrainCoachCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCoach.ScheduleTrainCoachDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCoach.ScheduleTrainCoachUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.GeneralStatus;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainCoach;
import com.easyrailjourney.EasyRailJourney.services.trainServices.ScheduleTrainCoachService;


@RestController
@RequestMapping("/schedule/coach")
public class ScheduleTrainCoachController {

     private final ScheduleTrainCoachService scheduleTrainCoachService;

     ScheduleTrainCoachController(ScheduleTrainCoachService scheduleTrainCoachService){
        this.scheduleTrainCoachService = scheduleTrainCoachService;
     }

     @PostMapping
    public ResponseEntity<GeneralScheduleTrainCoachRespDto>
            createScheduleTrainCoach(
                    @RequestBody ScheduleTrainCoachCreateReqDto reqDto) {

        GeneralScheduleTrainCoachRespDto respDto =
                new GeneralScheduleTrainCoachRespDto();

        try {

            ScheduleTrainCoach entity =
                    scheduleTrainCoachService
                            .createScheduleTrainCoach(reqDto);

            respDto.setScheduleTrainCoach(entity);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }


    // GET ALL SCHEDULE TRAIN COACH----
    @GetMapping
    public ResponseEntity<GeneralScheduleTrainCoachRespDto>
            getAllScheduleTrainCoaches() {

        GeneralScheduleTrainCoachRespDto respDto =
                new GeneralScheduleTrainCoachRespDto();

        try {

            List<ScheduleTrainCoach> coaches =
                    scheduleTrainCoachService
                            .getAllScheduleTrainCoaches();

            respDto.setScheduleTrainCoaches(coaches);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }


    // SEARCH SCHEDULE TRAIN COACH----
    @GetMapping("/search")
    public ResponseEntity<GeneralScheduleTrainCoachRespDto>
            searchScheduleTrainCoach(
                    @RequestParam(required = false) Long id,
                    @RequestParam(required = false) Long scheduleTrainClassId,
                    @RequestParam(required = false) Long coachId,
                    @RequestParam(required = false) String coachPosition,
                    @RequestParam(required = false) GeneralStatus status) {

        GeneralScheduleTrainCoachRespDto respDto =
                new GeneralScheduleTrainCoachRespDto();

        if (id == null &&
                scheduleTrainClassId == null &&
                coachId == null &&
                coachPosition == null &&
                status == null) {

            return ResponseEntity.notFound().build();
        }

        try {

            List<ScheduleTrainCoach> coaches =
                    scheduleTrainCoachService
                            .searchScheduleTrainCoach(
                                    id,
                                    scheduleTrainClassId,
                                    coachId,
                                    coachPosition,
                                    status
                            );

            respDto.setScheduleTrainCoaches(coaches);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }


    // UPDATE SCHEDULE TRAIN COACH----
    @PutMapping
    public ResponseEntity<GeneralScheduleTrainCoachRespDto>
            updateScheduleTrainCoach(
                    @RequestBody ScheduleTrainCoachUpdateReqDto reqDto) {

        GeneralScheduleTrainCoachRespDto respDto =
                new GeneralScheduleTrainCoachRespDto();

        try {

            boolean ans =
                    scheduleTrainCoachService
                            .updateScheduleTrainCoach(reqDto);

            if (ans) {

                respDto.setMessage("Successfully Updated");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);

            } else {

                respDto.setMessage("Not Updated");
                respDto.setResponseStatus(ResponseStatus.FAILURE);
            }

        } catch (Exception e) {

            respDto.setMessage("Failed Updated");
            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.badRequest().body(respDto);
    }


    // SOFT DELETE SCHEDULE TRAIN COACH----
    @DeleteMapping
    public ResponseEntity<GeneralScheduleTrainCoachRespDto>
            deleteScheduleTrainCoach(
                    @RequestBody ScheduleTrainCoachDeleteReqDto reqDto) {

        GeneralScheduleTrainCoachRespDto respDto =
                new GeneralScheduleTrainCoachRespDto();

        try {

            boolean ans =
                    scheduleTrainCoachService
                            .deleteScheduleTrainCoach(reqDto);

            if (ans) {

                respDto.setMessage("Deleted Successfully");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);

            } else {

                respDto.setMessage("Not Deleted");
                respDto.setResponseStatus(ResponseStatus.FAILURE);
            }

        } catch (Exception e) {

            respDto.setMessage(
                    "Delete Interruption " + e.getMessage()
            );

            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.ok().body(respDto);
    }


    // PERMANENT DELETE SCHEDULE TRAIN COACH----
    @DeleteMapping("/delete-permanently")
    public ResponseEntity<GeneralScheduleTrainCoachRespDto>
            deleteScheduleTrainCoachPermanently(
                    @RequestBody ScheduleTrainCoachDeleteReqDto reqDto) {

        GeneralScheduleTrainCoachRespDto respDto =
                new GeneralScheduleTrainCoachRespDto();

        try {

            boolean ans =
                    scheduleTrainCoachService
                            .deleteScheduleTrainCoachPermanently(reqDto);

            if (ans) {

                respDto.setMessage("Deleted Successfully");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);

            } else {

                respDto.setMessage("Not Deleted");
                respDto.setResponseStatus(ResponseStatus.FAILURE);
            }

        } catch (Exception e) {

            respDto.setMessage(
                    "Delete Interruption " + e.getMessage()
            );

            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.ok().body(respDto);
    }



    
}
