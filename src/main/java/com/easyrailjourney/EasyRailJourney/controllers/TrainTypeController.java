package com.easyrailjourney.EasyRailJourney.controllers;

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

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.GeneralTrainTypeRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrainTypeDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrainTypeReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrainTypeUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.services.trainServices.TrainTypeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/train-type")
@RequiredArgsConstructor
public class TrainTypeController {

    private final TrainTypeService trainTypeService;

    // CREATE
    @PostMapping
        @PreAuthorize("hasAuthority('ADD_TRAIN_TYPE')")
    public ResponseEntity<GeneralTrainTypeRespDto> createTrainType(
            @Valid @RequestBody TrainTypeReqDto dto) {

        return ResponseEntity.ok(
                trainTypeService.createTrainType(dto)
        );
    }

    // GET ALL
    @GetMapping
    @PreAuthorize("hasAuthority('READ_TRAIN_TYPE')")
    public ResponseEntity<GeneralTrainTypeRespDto> getAllTrainTypes() {

        return ResponseEntity.ok(
                trainTypeService.getAllTrainTypes()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_TRAIN_TYPE')")
    public ResponseEntity<GeneralTrainTypeRespDto> getTrainTypeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                trainTypeService.getTrainTypeById(id)
        );
    }

    // UPDATE
    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_TRAIN_TYPE')")
    public ResponseEntity<GeneralTrainTypeRespDto> updateTrainType(
            @Valid @RequestBody TrainTypeUpdateReqDto dto) {

        return ResponseEntity.ok(
                trainTypeService.updateTrainType(dto)
        );
    }

    // DELETE
    @DeleteMapping
    @PreAuthorize("hasAuthority('DELETE_TRAIN_TYPE')")
    public ResponseEntity<GeneralTrainTypeRespDto> deleteTrainType(
            @Valid @RequestBody TrainTypeDeleteReqDto dto) {

        return ResponseEntity.ok(
                trainTypeService.deleteTrainType(dto)
        );
    }
}