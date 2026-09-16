package com.easyrailjourney.EasyRailJourney.controllers;

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

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Seats.GeneralSeatRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Seats.SeatCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Seats.SeatDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Seats.SeatUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Seat;
import com.easyrailjourney.EasyRailJourney.services.trainServices.SeatService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/seat")
public class SeatController {

    private final SeatService seatService;

    SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // CREATE SEAT----
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_SEAT')")
    public ResponseEntity<GeneralSeatRespDto> createSeat(
           @Valid  @RequestBody SeatCreateReqDto reqDto) {

        GeneralSeatRespDto respDto = new GeneralSeatRespDto();

        try {

            Seat seat = seatService.createSeat(reqDto);

            respDto.setSeat(seat);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // GET ALL SEATS----
    @GetMapping
    @PreAuthorize("hasAuthority('READ_SEAT')")
    public ResponseEntity<GeneralSeatRespDto> getAllSeats() {

        GeneralSeatRespDto respDto = new GeneralSeatRespDto();

        try {

            List<Seat> seats =  seatService.getAllSeats();

            respDto.setSeats(seats);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // SEARCH SEAT----
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('READ_SEAT')")
    public ResponseEntity<GeneralSeatRespDto> searchSeat(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long coachId,
            @RequestParam(required = false) String seatNumber,
            @RequestParam(required = false) Long seatTypeId,
            @RequestParam(defaultValue = "false") boolean isDeleted) {

        GeneralSeatRespDto respDto = new GeneralSeatRespDto();

        if (id == null &&
                coachId == null &&
                seatNumber == null &&
                seatTypeId == null) {

            return ResponseEntity.notFound().build();
        }

        try {

            List<Seat> seats = 
                    seatService.searchSeat(
                            id,
                            coachId,
                            seatNumber,
                            seatTypeId,
                            isDeleted
                    );

            respDto.setSeats(seats);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // UPDATE SEAT----
    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_SEAT')")
    public ResponseEntity<GeneralSeatRespDto> updateSeat(
        @Valid  @RequestBody SeatUpdateReqDto reqDto) {

        GeneralSeatRespDto respDto = new GeneralSeatRespDto();

        try {

            boolean ans = seatService.updateSeat(reqDto);

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

    // SOFT DELETE SEAT----
    @DeleteMapping
    @PreAuthorize("hasAuthority('DELETE_SEAT')")
    public ResponseEntity<GeneralSeatRespDto> deleteSeat(
        @Valid  @RequestBody SeatDeleteReqDto reqDto) {

        GeneralSeatRespDto respDto = new GeneralSeatRespDto();

        try {

            boolean ans = seatService.deleteSeat(reqDto);

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

    // DELETE PERMANENTLY SEAT----
    @DeleteMapping("/delete-permanently")
    @PreAuthorize("hasAuthority('DELETE_SEAT')")
    public ResponseEntity<GeneralSeatRespDto> deleteSeatPermanently(
        @Valid  @RequestBody SeatDeleteReqDto reqDto) {

        GeneralSeatRespDto respDto = new GeneralSeatRespDto();

        try {

            boolean ans =
                    seatService.deleteSeatPermanently(reqDto);

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