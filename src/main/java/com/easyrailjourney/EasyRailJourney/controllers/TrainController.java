package com.easyrailjourney.EasyRailJourney.controllers;



import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.GeneralTrainRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrainCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrainDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrainUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;
import com.easyrailjourney.EasyRailJourney.services.trainServices.TrainService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/train")
public class TrainController {

    private final TrainService trainService;

    TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    // CREATE TRAIN----
    @PostMapping
            @PreAuthorize("hasAuthority('ADD_TRAIN')")
    public ResponseEntity<GeneralTrainRespDto> createTrain(
           @Valid @RequestBody TrainCreateReqDto reqDto) {

        GeneralTrainRespDto respDto = new GeneralTrainRespDto();

        try {

            Train train = trainService.createTrain(reqDto);

            respDto.getTrains().add(train);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);
            respDto.setMessage("Train Created Successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(respDto);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());

            return ResponseEntity.badRequest().body(respDto);
        }
    }

    // GET ALL TRAIN----
    @GetMapping
    @PreAuthorize("hasAuthority('READ_TRAIN')")
    public ResponseEntity<GeneralTrainRespDto> getAllTrains() {

        GeneralTrainRespDto respDto = new GeneralTrainRespDto();

        try {

            List<Train> trains = trainService.getAllTrains() ;

            respDto.setTrains(trains);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

            return ResponseEntity.ok().body(respDto);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());

            return ResponseEntity.badRequest().body(respDto);
        }
    }

    // SEARCH TRAIN----
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('READ_TRAIN')")
    public ResponseEntity<GeneralTrainRespDto> searchTrain(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String trainNumber,
            @RequestParam(required = false) String trainName,
            @RequestParam(required = false) String trainTypeName,
            @RequestParam(defaultValue = "false") boolean isDeleted) {

        GeneralTrainRespDto respDto = new GeneralTrainRespDto();

        if (id == null &&
                trainNumber == null &&
                trainName == null &&
              trainTypeName == null) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(
                    "At least one search parameter is required"
            );

            return ResponseEntity.badRequest().body(respDto);
        }

        try {

            List<Train> trains =
                    trainService.searchTrain(
                            id,
                            trainNumber,
                            trainName,
                            trainTypeName,
                            isDeleted
                    );

            respDto.setTrains(trains);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // UPDATE TRAIN----
    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_TRAIN')")
    public ResponseEntity<GeneralTrainRespDto> updateTrain(
        @Valid  @RequestBody TrainUpdateReqDto reqDto) {

        GeneralTrainRespDto respDto = new GeneralTrainRespDto();

        try {

            boolean ans = trainService.updateTrain(reqDto);

            if (ans) {
                respDto.setMessage("Successfully Updated");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);
            } else {
                respDto.setMessage("Not Updated");
                respDto.setResponseStatus(ResponseStatus.FAILURE);
            }

        } catch (Exception e) {

            respDto.setMessage("Update Interruption " + e.getMessage());
            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.ok().body(respDto);
    }

    // DELETE SOFT TRAIN----
    @DeleteMapping
    @PreAuthorize("hasAuthority('DELETE_TRAIN')")
    public ResponseEntity<GeneralTrainRespDto> deleteTrain(
        @Valid  @RequestBody TrainDeleteReqDto reqDto) {

        GeneralTrainRespDto respDto = new GeneralTrainRespDto();

        try {

            boolean ans = trainService.deleteTrain(reqDto);

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

    // DELETE PERMANENTLY TRAIN----
    @DeleteMapping("/delete-permanently")
    @PreAuthorize("hasAuthority('DELETE_TRAIN')")
    public ResponseEntity<GeneralTrainRespDto> deleteTrainPermanently(
        @Valid  @RequestBody TrainDeleteReqDto reqDto) {

        GeneralTrainRespDto respDto = new GeneralTrainRespDto();

        try {

            boolean ans =
                    trainService.deleteTrainPermanently(reqDto);

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