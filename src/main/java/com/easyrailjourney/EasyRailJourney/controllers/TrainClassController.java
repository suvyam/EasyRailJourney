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

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrnClass.GeneralTrainClassRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrnClass.TrainClassCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrnClass.TrainClassDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrnClass.TrainClassUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainClass;
import com.easyrailjourney.EasyRailJourney.services.trainServices.TrainClassService;

@RestController
@RequestMapping("/train-class")
public class TrainClassController {

    private final TrainClassService trainClassService;

    TrainClassController(TrainClassService trainClassService) {
        this.trainClassService = trainClassService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<GeneralTrainClassRespDto> createTrainClass(
            @RequestBody TrainClassCreateReqDto reqDto) {

        GeneralTrainClassRespDto respDto =
                new GeneralTrainClassRespDto();

        try {

            TrainClass trainClass =
                    trainClassService.createTrainClass(reqDto);

            respDto.setTrainClass(trainClass);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<GeneralTrainClassRespDto>
            getAllTrainClasses() {

        GeneralTrainClassRespDto respDto =
                new GeneralTrainClassRespDto();

        try {

            List<TrainClass> trainClasses = 
                    trainClassService.getAllTrainClasses();

            respDto.setTrainClasses(trainClasses);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // SEARCH
    @GetMapping("/search")
    public ResponseEntity<GeneralTrainClassRespDto>
            searchTrainClass(
                    @RequestParam(required = false) Long id,
                    @RequestParam(required = false) String classCode,
                    @RequestParam(required = false) String className,
                    @RequestParam(defaultValue = "false")
                    boolean isDeleted) {

        GeneralTrainClassRespDto respDto =
                new GeneralTrainClassRespDto();

        if (id == null &&
                classCode == null &&
                className == null) {

            return ResponseEntity.notFound().build();
        }

        try {

         List<TrainClass> trainClasses =
                    trainClassService.searchTrainClass(
                            id,
                            classCode,
                            className,
                            isDeleted
                    );

            respDto.setTrainClasses(trainClasses);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<GeneralTrainClassRespDto>
            updateTrainClass(
                    @RequestBody TrainClassUpdateReqDto reqDto) {

        GeneralTrainClassRespDto respDto =
                new GeneralTrainClassRespDto();

        try {

            boolean ans =
                    trainClassService.updateTrainClass(reqDto);

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

    // SOFT DELETE
    @DeleteMapping
    public ResponseEntity<GeneralTrainClassRespDto>
            deleteTrainClass(
                    @RequestBody TrainClassDeleteReqDto reqDto) {

        GeneralTrainClassRespDto respDto =
                new GeneralTrainClassRespDto();

        try {

            boolean ans =
                    trainClassService.deleteTrainClass(reqDto);

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

    // PERMANENT DELETE
    @DeleteMapping("/delete-permanently")
    public ResponseEntity<GeneralTrainClassRespDto>
            deleteTrainClassPermanently(
                    @RequestBody TrainClassDeleteReqDto reqDto) {

        GeneralTrainClassRespDto respDto =
                new GeneralTrainClassRespDto();

        try {

            boolean ans =
                    trainClassService.deleteTrainClassPermanently(reqDto);

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