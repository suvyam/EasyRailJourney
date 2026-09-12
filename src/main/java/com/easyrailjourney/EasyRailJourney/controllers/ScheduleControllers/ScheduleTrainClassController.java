package com.easyrailjourney.EasyRailJourney.controllers.ScheduleControllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses.GeneralScheduleTrainClassRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses.GeneralScheduleTrainClassSeatRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses.ScheduleTrainClassCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses.ScheduleTrainClassDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses.ScheduleTrainClassRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses.ScheduleTrainClassUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;
import com.easyrailjourney.EasyRailJourney.services.trainServices.ScheduleTrainClassService;


@RestController 
@RequestMapping ("/class")
public class ScheduleTrainClassController {

       private final ScheduleTrainClassService scheduleTrainClassService;

       ScheduleTrainClassController(ScheduleTrainClassService scheduleTrainClassService){
        this.scheduleTrainClassService = scheduleTrainClassService;
       }

     // CREATE SCHEDULE TRAIN CLASS----
    @PostMapping
    public ResponseEntity<GeneralScheduleTrainClassRespDto>
            createScheduleTrainClass(
                    @RequestBody ScheduleTrainClassCreateReqDto reqDto) {

        GeneralScheduleTrainClassRespDto respDto =
                new GeneralScheduleTrainClassRespDto();

        try {

            ScheduleTrainClassRespDto entity =
                    scheduleTrainClassService
                            .createScheduleTrainClass(reqDto);

            respDto.setScheduleTrainClass(entity);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(respDto);
    }


    // GET ALL SCHEDULE TRAIN CLASSES----
    @GetMapping
    public ResponseEntity<GeneralScheduleTrainClassRespDto>
            getAllScheduleTrainClasses() {

        GeneralScheduleTrainClassRespDto respDto =
                new GeneralScheduleTrainClassRespDto();

        try {

            List<ScheduleTrainClassRespDto> entity =
                    scheduleTrainClassService
                            .getAllScheduleTrainClasses();

            respDto.setScheduleTrainClasses(entity);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(respDto);
    }


    // SEARCH SCHEDULE TRAIN CLASS----
    @GetMapping("/search")
    public ResponseEntity<GeneralScheduleTrainClassRespDto>
            searchScheduleTrainClass(
                    @RequestParam(required = false) Long id,
                    @RequestParam(required = false) Long scheduleTrainId,
                    @RequestParam(required = false) Long trainClassId) {

        GeneralScheduleTrainClassRespDto respDto =
                new GeneralScheduleTrainClassRespDto();

        if (id == null &&
                scheduleTrainId == null &&
                trainClassId == null) {

            return ResponseEntity.notFound().build();
        }

        try {

            List<ScheduleTrainClassRespDto> entities =
                    scheduleTrainClassService
                            .searchScheduleTrainClass(
                                    id,
                                    scheduleTrainId,
                                    trainClassId
                            );

            respDto.setScheduleTrainClasses(entities);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(respDto);
    }


    // UPDATE SCHEDULE TRAIN CLASS----
    @PutMapping
    public ResponseEntity<GeneralScheduleTrainClassRespDto>
            updateScheduleTrainClass(
                    @RequestBody ScheduleTrainClassUpdateReqDto reqDto) {

        GeneralScheduleTrainClassRespDto respDto =
                new GeneralScheduleTrainClassRespDto();

        try {

            boolean ans =
                    scheduleTrainClassService
                            .updateScheduleTrainClass(reqDto);

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


    // SOFT DELETE SCHEDULE TRAIN CLASS----
    @DeleteMapping
    public ResponseEntity<GeneralScheduleTrainClassRespDto>
            deleteScheduleTrainClass(
                    @RequestBody ScheduleTrainClassDeleteReqDto reqDto) {

        GeneralScheduleTrainClassRespDto respDto =
                new GeneralScheduleTrainClassRespDto();

        try {

            boolean ans =
                    scheduleTrainClassService
                            .deleteScheduleTrainClass(reqDto);

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

        return ResponseEntity.ok(respDto);
    }


    // PERMANENT DELETE SCHEDULE TRAIN CLASS----
    @DeleteMapping("/delete-permanently")
    public ResponseEntity<GeneralScheduleTrainClassRespDto>
            deleteScheduleTrainClassPermanently(
                    @RequestBody ScheduleTrainClassDeleteReqDto reqDto) {

        GeneralScheduleTrainClassRespDto respDto =
                new GeneralScheduleTrainClassRespDto();

        try {

            boolean ans =
                    scheduleTrainClassService
                            .deleteScheduleTrainClassPermanently(reqDto);

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

        return ResponseEntity.ok(respDto);
    }


    // GET SCHEDULE TRAIN CLASS SEATS----
    @GetMapping("/{scheduleTrainClassId}/seats")
    public ResponseEntity<GeneralScheduleTrainClassSeatRespDto>
            getSeatsByScheduleTrainClass(
                    @PathVariable Long scheduleTrainClassId) {

        GeneralScheduleTrainClassSeatRespDto respDto =
                new GeneralScheduleTrainClassSeatRespDto();

        try {

            List<ScheduleTrainClassSeat> seats =
                    scheduleTrainClassService
                            .getSeatsByScheduleTrainClass(
                                    scheduleTrainClassId
                            );

            respDto.setScheduleTrainClassSeats(seats);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }



};
    
