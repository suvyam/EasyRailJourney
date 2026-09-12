package com.easyrailjourney.EasyRailJourney.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CoachType> createCoachType(
            @RequestBody CoachType coachType) {

        return ResponseEntity.ok(
                coachTypeService.createCoachType(coachType)
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<CoachType>> getAllCoachTypes() {

        return ResponseEntity.ok(
                coachTypeService.getAllCoachTypes()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CoachType> getCoachTypeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                coachTypeService.getCoachTypeById(id)
        );
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<CoachType> updateCoachType(
            @RequestBody CoachType coachType) {

        return ResponseEntity.ok(
                coachTypeService.updateCoachType(coachType)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoachType(
            @PathVariable Long id) {

        coachTypeService.deleteCoachType(id);

        return ResponseEntity.ok(
                "CoachType deleted successfully"
        );
    }
}