package com.easyrailjourney.EasyRailJourney.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachSeatDtos.AssignCoachSeatReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachSeatDtos.CoachSeatRespDto;
import com.easyrailjourney.EasyRailJourney.services.trainServices.CoachSeatService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/coach-seat")
public class CoachSeatController {

    private final CoachSeatService coachSeatService;

    public CoachSeatController(
        CoachSeatService coachSeatService) {

        this.coachSeatService = coachSeatService;
    }

    // CREATE / ASSIGN
    @PostMapping
    public ResponseEntity<CoachSeatRespDto > assignSeats(
            @Valid @RequestBody AssignCoachSeatReqDto dto) {

        return ResponseEntity.ok(
                coachSeatService.assignSeats(dto)
        );
    }

    // GET ALL SEATS OF COACH
    @GetMapping("/coach/{coachId}")
    public ResponseEntity<CoachSeatRespDto> getSeatsByCoachId(
            @PathVariable Long coachId) {

        return ResponseEntity.ok(
                coachSeatService.getSeatsByCoachId(coachId)
        );
    }
    
    // GET COACH OF SEAT
    @GetMapping("/seat/{seatId}")
    public ResponseEntity<CoachSeatRespDto> getCoachBySeat(
            @PathVariable Long seatId) {

        return ResponseEntity.ok(
                coachSeatService.getCoachBySeatId(seatId)
        );
    }

    // UPDATE
    @PutMapping("/{coachId}")
    public ResponseEntity<CoachSeatRespDto> updateSeats(
            @PathVariable Long coachId,
            @Valid @RequestBody AssignCoachSeatReqDto dto) {

        return ResponseEntity.ok(
                coachSeatService.updateSeats(coachId, dto)
        );
    }

    // DELETE / UNASSIGN
    @DeleteMapping("/{coachId}/seat/{seatId}")
    public ResponseEntity<String> removeSeatFromCoach(
            @PathVariable Long coachId,
            @PathVariable Long seatId) {

        return ResponseEntity.ok(
                coachSeatService.removeSeatFromCoach(
                        coachId,
                        seatId
                )
        );
    }
}