package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.GeneralTrainTypeRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrainTypeDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrainTypeReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrainTypeUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainType;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.TrainTypeRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainTypeService {

    private final TrainTypeRepo trainTypeRepository;

    // CREATE
    @Transactional
    public GeneralTrainTypeRespDto createTrainType(TrainTypeReqDto dto) {

        GeneralTrainTypeRespDto response = new GeneralTrainTypeRespDto();

        try {

            if (trainTypeRepository.findByNameAndIsDeletedFalse(dto.getName()).isPresent()) {

                response.setResponseStatus(ResponseStatus.FAILURE);
                response.setMessage("Train type already exists");
                response.setTrainTypes(new ArrayList<>());

                return response;
            }

            TrainType trainType = new TrainType();
            trainType.setName(dto.getName());

            TrainType savedTrainType = trainTypeRepository.save(trainType);

            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("Train type created successfully");
            response.setTrainTypes(List.of(savedTrainType));

        } catch (Exception e) {

            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
            response.setTrainTypes(new ArrayList<>());
        }

        return response;
    }

    // GET ALL
    @Transactional(readOnly = true)
    public GeneralTrainTypeRespDto getAllTrainTypes() {

        GeneralTrainTypeRespDto response = new GeneralTrainTypeRespDto();

        try {

            List<TrainType> trainTypes =
                    trainTypeRepository.findAll();

            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("Train types fetched successfully");
            response.setTrainTypes(trainTypes);

        } catch (Exception e) {

            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
            response.setTrainTypes(new ArrayList<>());
        }

        return response;
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public GeneralTrainTypeRespDto getTrainTypeById(Long id) {

        GeneralTrainTypeRespDto response = new GeneralTrainTypeRespDto();

        try {

            TrainType trainType = trainTypeRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Train type not found"));

            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("Train type fetched successfully");
            response.setTrainTypes(List.of(trainType));

        } catch (Exception e) {

            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
            response.setTrainTypes(new ArrayList<>());
        }

        return response;
    }

    // UPDATE
    @Transactional
    public GeneralTrainTypeRespDto updateTrainType(
            TrainTypeUpdateReqDto dto) {

        GeneralTrainTypeRespDto response = new GeneralTrainTypeRespDto();

        try {

            TrainType trainType = trainTypeRepository.findById(dto.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Train type not found"));

            if (trainType.getIsDeleted()) {
                throw new RuntimeException("Train type is deleted");
            }

            if (dto.getName() != null && !dto.getName().isBlank()) {

                trainTypeRepository.findByNameAndIsDeletedFalse(dto.getName())
                        .ifPresent(existing -> {

                            if (!existing.getId().equals(dto.getId())) {
                                throw new RuntimeException(
                                        "Train type name already exists");
                            }
                        });

                trainType.setName(dto.getName());
            }

            TrainType updatedTrainType =
                    trainTypeRepository.save(trainType);

            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("Train type updated successfully");
            response.setTrainTypes(List.of(updatedTrainType));

        } catch (Exception e) {

            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
            response.setTrainTypes(new ArrayList<>());
        }

        return response;
    }

    // SOFT DELETE
    @Transactional
    public GeneralTrainTypeRespDto deleteTrainType(
            TrainTypeDeleteReqDto dto) {

        GeneralTrainTypeRespDto response = new GeneralTrainTypeRespDto();

        try {

            TrainType trainType = trainTypeRepository.findById(dto.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Train type not found"));

            if (trainType.getIsDeleted()) {
                throw new RuntimeException("Train type already deleted");
            }

            trainType.setIsDeleted(true);

            TrainType deletedTrainType =
                    trainTypeRepository.save(trainType);

            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("Train type deleted successfully");
            response.setTrainTypes(List.of(deletedTrainType));

        } catch (Exception e) {

            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
            response.setTrainTypes(new ArrayList<>());
        }

        return response;
    }
}