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

import com.easyrailjourney.EasyRailJourney.Dtos.StationDtos.GeneralStationRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.StationDtos.StationCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.StationDtos.StationDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.StationDtos.StationUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Station;
import com.easyrailjourney.EasyRailJourney.services.trainServices.StationService;

@RestController
@RequestMapping("/station")
public class StationController {

    private final StationService stationService;
    

    StationController(StationService stationService) {
        this.stationService = stationService;
    }

    // CREATE STATION----
    @PostMapping
    public ResponseEntity<GeneralStationRespDto> createStation(
            @RequestBody StationCreateReqDto reqDto) {

        GeneralStationRespDto respDto =
                new GeneralStationRespDto();

        try {

            Station station =
                    stationService.createStation(reqDto);

            respDto.setStation(station);
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

    // GET ALL STATIONS----
    @GetMapping
    public ResponseEntity<GeneralStationRespDto> getAllStations() {

        GeneralStationRespDto respDto =
                new GeneralStationRespDto();

        try {

            List<Station> stations =
                    stationService.getAllStations();

            respDto.setStations(stations);
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

    // SEARCH STATION----
    @GetMapping("/search")
    public ResponseEntity<GeneralStationRespDto> searchStation(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "false")
            boolean isDeleted) {

        GeneralStationRespDto respDto =
                new GeneralStationRespDto();

        if (id == null &&
                code == null &&
                name == null) {

            return ResponseEntity.notFound().build();
        }

        try {

            List<Station> stations =
                    stationService.searchStation(
                            id,
                            code,
                            name,
                            isDeleted
                    );

            respDto.setStations(stations);
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

    // UPDATE STATION----
    @PutMapping
    public ResponseEntity<GeneralStationRespDto> updateStation(
            @RequestBody StationUpdateReqDto reqDto) {

        GeneralStationRespDto respDto =
                new GeneralStationRespDto();

        try {

            boolean ans =
                    stationService.updateStation(reqDto);

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

    // SOFT DELETE STATION----
    @DeleteMapping
    public ResponseEntity<GeneralStationRespDto> deleteStation(
            @RequestBody StationDeleteReqDto reqDto) {

        GeneralStationRespDto respDto =
                new GeneralStationRespDto();

        try {

            boolean ans =
                    stationService.deleteStation(reqDto);

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

    // DELETE PERMANENTLY STATION----
    @DeleteMapping("/delete-permanently")
    public ResponseEntity<GeneralStationRespDto>
            deleteStationPermanently(
                    @RequestBody StationDeleteReqDto reqDto) {

        GeneralStationRespDto respDto =
                new GeneralStationRespDto();

        try {

            boolean ans =
                    stationService.deleteStationPermanently(
                            reqDto
                    );

            if (!ans) {

                respDto.setMessage("Not Deleted");

                respDto.setResponseStatus(ResponseStatus.FAILURE);

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