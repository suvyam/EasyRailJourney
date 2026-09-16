package com.easyrailjourney.EasyRailJourney.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.CoachType;
import com.easyrailjourney.EasyRailJourney.services.trainServices.CoachTypeService;

import jakarta.validation.Valid;




@RestController
@RequestMapping("/coach-type")
public class CoachTypeController {

    private final CoachTypeService coachTypeService;

    public CoachTypeController(
            CoachTypeService coachTypeService) {

        this.coachTypeService = coachTypeService;
    }

    // CREATE
    @PostMapping
    @PreAuthorize("hasAuthority('ADD_COACH_TYPE')")
    public ResponseEntity<CoachType> createCoachType(
          @Valid   @RequestBody CoachType coachType) {

        return ResponseEntity.ok(
                coachTypeService.createCoachType(coachType)
        );
    }

    // GET ALL
    @GetMapping
    @PreAuthorize("hasAuthority('READ_COACH_TYPE')")
    public ResponseEntity<List<CoachType>> getAllCoachTypes() {

        return ResponseEntity.ok(
                coachTypeService.getAllCoachTypes()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_COACH_TYPE')")
    public ResponseEntity<CoachType> getCoachTypeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                coachTypeService.getCoachTypeById(id)
        );
    }

    // UPDATE
    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_COACH_TYPE')")
    public ResponseEntity<CoachType> updateCoachType(
        @Valid  @RequestBody CoachType coachType) {

        return ResponseEntity.ok(
                coachTypeService.updateCoachType(coachType)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_COACH_TYPE')")
    public ResponseEntity<String> deleteCoachType(
            @PathVariable Long id) {

        coachTypeService.deleteCoachType(id);

        return ResponseEntity.ok(
                "CoachType deleted successfully"
        );
    }
}