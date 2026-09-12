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

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.SeatType.*;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.SeatType;
import com.easyrailjourney.EasyRailJourney.services.trainServices.SeatTypeService;

@RestController
@RequestMapping("/seat-type")
public class SeatTypeController {

    private final SeatTypeService seatTypeService;

    SeatTypeController(SeatTypeService seatTypeService) {
        this.seatTypeService = seatTypeService;
    }


    // CREATE SEAT TYPE----
    @PostMapping
    public ResponseEntity<SeatTypeRespDto> createSeatType(
            @RequestBody SeatTypeReqDto reqDto) {

                SeatTypeRespDto respDto =
                new SeatTypeRespDto();

        try {

            SeatType seatType =
                    seatTypeService.createSeatType(reqDto);

            respDto.setSeatType(seatType);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }


    // GET ALL SEAT TYPES----
    @GetMapping
    public ResponseEntity<SeatTypeRespDto> getAllSeatTypes() {

        SeatTypeRespDto respDto =
                new SeatTypeRespDto();

        try {

            List<SeatType> seatTypes =
                    seatTypeService.getAllSeatTypes();

            respDto.setSeatTypes(seatTypes);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }


    // SEARCH SEAT TYPE----
    @GetMapping("/search")
    public ResponseEntity<SeatTypeRespDto> searchSeatType(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String typeCode,
            @RequestParam(required = false) String typeName,
            @RequestParam(defaultValue = "false") boolean isDeleted) {

                SeatTypeRespDto respDto =
                new SeatTypeRespDto();

        if (id == null &&
                typeCode == null &&
                typeName == null) {

            return ResponseEntity.notFound().build();
        }

        try {

            List<SeatType> seatTypes =
                    seatTypeService.searchSeatType(
                            id,
                            typeCode,
                            typeName,
                            isDeleted
                    );

            respDto.setSeatTypes(seatTypes);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }


    // UPDATE SEAT TYPE----
    @PutMapping
    public ResponseEntity<SeatTypeRespDto> updateSeatType(
            @RequestBody SeatTypeReqDto reqDto) {

                SeatTypeRespDto respDto =
                new SeatTypeRespDto();

        try {

            boolean ans =
                    seatTypeService.updateSeatType(reqDto);

            if (ans) {

                respDto.setMessage("Successfully Updated");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);

            } else {

                respDto.setMessage("Not Updated");
                respDto.setResponseStatus(ResponseStatus.FAILURE);
            }

        } catch (Exception e) {

            respDto.setMessage(
                    "Failed Updated " + e.getMessage()
            );

            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.badRequest().body(respDto);
    }


    // SOFT DELETE SEAT TYPE----
    @DeleteMapping
    public ResponseEntity<SeatTypeRespDto> deleteSeatType(
            @RequestBody SeatTypeReqDto reqDto) {

                SeatTypeRespDto respDto =
                new SeatTypeRespDto();

        try {

            boolean ans =
                    seatTypeService.deleteSeatType(reqDto);

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


    // DELETE PERMANENTLY SEAT TYPE----
    @DeleteMapping("/delete-permanently")
    public ResponseEntity<SeatTypeRespDto>
            deleteSeatTypePermanently(
                    @RequestBody SeatTypeReqDto reqDto) {

                        SeatTypeRespDto respDto =
                new SeatTypeRespDto();

        try {

            boolean ans =
                    seatTypeService.deleteSeatTypePermanently(
                            reqDto
                    );

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
}