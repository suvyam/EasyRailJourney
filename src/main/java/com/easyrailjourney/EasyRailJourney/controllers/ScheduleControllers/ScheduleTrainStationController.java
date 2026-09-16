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

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation.CreateAllScheduleTrainStation;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation.GeneralScheduleTrainStationRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation.ScheduleTrainStationDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation.ScheduleTrainStationUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainStation;
import com.easyrailjourney.EasyRailJourney.services.trainServices.ScheduleTrainStationService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("schedule/station")
public class ScheduleTrainStationController {

    private final ScheduleTrainStationService scheduleTrainStationService;

    ScheduleTrainStationController(ScheduleTrainStationService scheduleTrainStationService){
    this.scheduleTrainStationService=scheduleTrainStationService;
    }


    // CREATE SCHEDULE TRAIN STATION----
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_SCHEDULE_TRAIN_STATION')")
    public ResponseEntity<GeneralScheduleTrainStationRespDto>
            createScheduleTrainStation(
                  @Valid   @RequestBody CreateAllScheduleTrainStation reqDto) {

        GeneralScheduleTrainStationRespDto respDto =
                new GeneralScheduleTrainStationRespDto();

        try {

            Boolean entity =
                    scheduleTrainStationService
                            .createScheduleTrainStation(reqDto);

            respDto.setIsCreated(entity);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }


    // GET ALL SCHEDULE TRAIN STATIONS----
    @GetMapping
    @PreAuthorize("hasAuthority('READ_SCHEDULE_TRAIN_STATION')")
    public ResponseEntity<GeneralScheduleTrainStationRespDto>
            getAllScheduleTrainStations() {

        GeneralScheduleTrainStationRespDto respDto =
                new GeneralScheduleTrainStationRespDto();

        try {

            List<ScheduleTrainStation> stations =
                    scheduleTrainStationService
                            .getAllScheduleTrainStations();

            respDto.setScheduleTrainStations(stations);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }


    // SEARCH SCHEDULE TRAIN STATION----
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('READ_SCHEDULE_TRAIN_STATION')")
    public ResponseEntity<GeneralScheduleTrainStationRespDto>
            searchScheduleTrainStation(
                    @RequestParam(required = false) Long id,
                    @RequestParam(required = false) Long scheduleTrainId,
                    @RequestParam(required = false) Long stationId,
                    @RequestParam(required = false) Integer stationSequence) {

        GeneralScheduleTrainStationRespDto respDto =
                new GeneralScheduleTrainStationRespDto();

                if (id == null &&
                    scheduleTrainId == null &&
                    stationId == null &&
                    stationSequence == null) {
                
                    return ResponseEntity.badRequest().build();
                }

        try {

            List<ScheduleTrainStation> stations =
                    scheduleTrainStationService
                            .searchScheduleTrainStation(
                                    id,
                                    scheduleTrainId,
                                    stationId,
                                    stationSequence
                            );

            respDto.setScheduleTrainStations(stations);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }


    // UPDATE SCHEDULE TRAIN STATION----
    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_SCHEDULE_TRAIN_STATION')")
    public ResponseEntity<GeneralScheduleTrainStationRespDto>
            updateScheduleTrainStation(
                @Valid   @RequestBody ScheduleTrainStationUpdateReqDto reqDto) {

        GeneralScheduleTrainStationRespDto respDto =
                new GeneralScheduleTrainStationRespDto();

        try {

            boolean ans =
                    scheduleTrainStationService
                            .updateScheduleTrainStation(reqDto);

            if (ans) {

                respDto.setMessage("Successfully Updated");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);
                return ResponseEntity.ok().body(respDto);

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


    // SOFT DELETE SCHEDULE TRAIN STATION----
    @DeleteMapping
    @PreAuthorize("hasAuthority('DELETE_SCHEDULE_TRAIN_STATION')")
    public ResponseEntity<GeneralScheduleTrainStationRespDto>
            deleteScheduleTrainStation(
                @Valid   @RequestBody ScheduleTrainStationDeleteReqDto reqDto) {

        GeneralScheduleTrainStationRespDto respDto =
                new GeneralScheduleTrainStationRespDto();

        try {

            boolean ans =
                    scheduleTrainStationService
                            .deleteScheduleTrainStation(reqDto);

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


    // PERMANENT DELETE SCHEDULE TRAIN STATION----
    @DeleteMapping("delete-permanently")
    @PreAuthorize("hasAuthority('DELETE_SCHEDULE_TRAIN_STATION')")
    public ResponseEntity<GeneralScheduleTrainStationRespDto>
            deleteScheduleTrainStationPermanently(
                @Valid   @RequestBody ScheduleTrainStationDeleteReqDto reqDto) {

        GeneralScheduleTrainStationRespDto respDto =
                new GeneralScheduleTrainStationRespDto();

        try {

            boolean ans =
                    scheduleTrainStationService
                            .deleteScheduleTrainStationPermanently(reqDto);

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


    @GetMapping("/History")
    @PreAuthorize("hasAuthority('READ_SCHEDULE_TRAIN_STATION_HISTORY')")
    public ResponseEntity<List<?>>getAllScheduleTrainStationsHistory(){
        try {
            return ResponseEntity.ok().body( scheduleTrainStationService.getAllScheduleTrainStationsHistory());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
   
    };


    @GetMapping("/History/search")
    @PreAuthorize("hasAuthority('READ_SCHEDULE_TRAIN_STATION_HISTORY')")
    public ResponseEntity<List<?>>getAllScheduleTrainStationsHistory(
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) Long scheduleTrainId,
        @RequestParam(required = false) Long stationId,
        @RequestParam(required = false) Integer stationSequence
    )
    {

        try {
            return  ResponseEntity.ok().body(scheduleTrainStationService.searchScheduleTrainStationHistorys(id, scheduleTrainId, stationId, stationSequence));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
            
        }
       
    };






}
