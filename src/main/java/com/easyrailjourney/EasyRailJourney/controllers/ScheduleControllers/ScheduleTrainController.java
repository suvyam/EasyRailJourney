package com.easyrailjourney.EasyRailJourney.controllers.ScheduleControllers;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.GeneralScheduleRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.enums.Trains.TrainStatus;
import com.easyrailjourney.EasyRailJourney.services.trainServices.ScheduleTrainService;


@RestController
@RequestMapping("/schedule")
public class ScheduleTrainController {

    private final ScheduleTrainService scheduleService;


    public ScheduleTrainController( ScheduleTrainService scheduleService){
        this.scheduleService = scheduleService;
    }


    // CREATE SCHEDULE----
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_SCHEDULE')")
    public ResponseEntity<GeneralScheduleRespDto> createSchedule(
            @RequestBody ScheduleCreateReqDto reqDto) {

        GeneralScheduleRespDto respDto = new GeneralScheduleRespDto();

        try {

            ScheduleRespDto schedule =
                    scheduleService.createSchedule(reqDto);

            respDto.setSchedule(schedule);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }


    // GET ALL SCHEDULE----
    @GetMapping
    @PreAuthorize("hasAuthority('READ_SCHEDULE')")
    public ResponseEntity<GeneralScheduleRespDto> getAllSchedules() {

        GeneralScheduleRespDto respDto = new GeneralScheduleRespDto();

        try {

            List<ScheduleRespDto> schedules =
                    scheduleService.getAllSchedules();

            respDto.setSchedules(schedules);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }


    // SEARCH SCHEDULE----
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('READ_SCHEDULE')")
    public ResponseEntity<GeneralScheduleRespDto> searchSchedule(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long trainId,
            @RequestParam(required = false) Long departureStationId,
            @RequestParam(required = false) Long destinationStationId,
            @RequestParam(required = false) TrainStatus status,
            @RequestParam(defaultValue = "false") boolean isDeleted) {

        GeneralScheduleRespDto respDto = new GeneralScheduleRespDto();

        if (id == null &&
                trainId == null &&
                departureStationId == null &&
                destinationStationId == null &&
                status == null) {

            return ResponseEntity.notFound().build();
        }

        try {

            List<ScheduleRespDto> schedules =
                    scheduleService.searchSchedule(
                            id,
                            trainId,
                            departureStationId,
                            destinationStationId,
                            status,
                            isDeleted
                    );

            respDto.setSchedules(schedules);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }


    // UPDATE SCHEDULE----
    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_SCHEDULE')")
    public ResponseEntity<GeneralScheduleRespDto> updateSchedule(
            @RequestBody ScheduleUpdateReqDto reqDto) {

        GeneralScheduleRespDto respDto = new GeneralScheduleRespDto();

        try {

            boolean ans = scheduleService.updateSchedule(reqDto);

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


    // DELETE PERMANENTLY SCHEDULE----
    @DeleteMapping("/delete-permanently")
    @PreAuthorize("hasAuthority('DELETE_SCHEDULE')")
    public ResponseEntity<GeneralScheduleRespDto> deleteSchedulePermanently(
            @RequestBody ScheduleDeleteReqDto reqDto) {

        GeneralScheduleRespDto respDto = new GeneralScheduleRespDto();

        try {

            boolean ans =
                    scheduleService.deleteSchedulePermanently(reqDto);

            if (!ans) {

                respDto.setMessage("Not Deleted");
                respDto.setResponseStatus(ResponseStatus.FAILURE);

            } else {

                respDto.setMessage("Deleted Successfully");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);
            }

        } catch (Exception e) {

            respDto.setMessage(
                    "Delete Interruption " + e.getMessage()
            );

            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.ok().body(respDto);
    }



};