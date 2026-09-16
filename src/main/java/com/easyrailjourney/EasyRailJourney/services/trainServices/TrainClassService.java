package com.easyrailjourney.EasyRailJourney.services.trainServices;
import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrnClass.TrainClassCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrnClass.TrainClassDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrnClass.TrainClassUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainClass;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.TrainClassRepo;

import jakarta.transaction.Transactional;

@Service
public class TrainClassService {

    private final TrainClassRepo trainClassRepo;

    public TrainClassService(TrainClassRepo trainClassRepo) {
        this.trainClassRepo = trainClassRepo;
    }

    // CREATE
   @Transactional 
    public TrainClass createTrainClass(
            TrainClassCreateReqDto reqDto) throws Exception {

        if (reqDto.getClassCode() == null ||
                reqDto.getClassCode().isBlank()) {

            throw new Exception("Class code is required");
        }

        if (reqDto.getClassName() == null ||
                reqDto.getClassName().isBlank()) {

            throw new Exception("Class name is required");
        }

        if (trainClassRepo.existsByClassCode(
                reqDto.getClassCode())) {

            throw new Exception("Class code already exists");
        }

        if (trainClassRepo.existsByClassName(
                reqDto.getClassName())) {

            throw new Exception("Class name already exists");
        }

        TrainClass trainClass = new TrainClass();

        trainClass.setClassCode(reqDto.getClassCode());
        trainClass.setClassName(reqDto.getClassName());
        trainClass.setDescription(reqDto.getDescription());
        trainClass.setDeleted(false);

        return trainClassRepo.save(trainClass);
    }

    // GET ALL
    public List<TrainClass> getAllTrainClasses() {

        return trainClassRepo.findAll();
    }

    // SEARCH
    public List<TrainClass> searchTrainClass(
            Long id,
            String classCode,
            String className,
            boolean isDeleted) {

        return trainClassRepo.searchTrainClass(
                id,
                classCode,
                className,
                isDeleted
        );
    }

    // UPDATE
    @Transactional 
    public boolean updateTrainClass(
            TrainClassUpdateReqDto reqDto) throws Exception {

        TrainClass trainClass =
                trainClassRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Train class not found"
                                ));

        if (reqDto.getClassCode() != null &&
                !reqDto.getClassCode()
                        .equals(trainClass.getClassCode())) {

            if (trainClassRepo.existsByClassCode(
                    reqDto.getClassCode())) {

                throw new Exception(
                        "Class code already exists"
                );
            }

            trainClass.setClassCode(
                    reqDto.getClassCode()
            );
        }

        if (reqDto.getClassName() != null &&
                !reqDto.getClassName()
                        .equals(trainClass.getClassName())) {

            if (trainClassRepo.existsByClassName(
                    reqDto.getClassName())) {

                throw new Exception(
                        "Class name already exists"
                );
            }

            trainClass.setClassName(
                    reqDto.getClassName()
            );
        }

        if (reqDto.getDescription() != null) {
            trainClass.setDescription(
                    reqDto.getDescription()
            );
        }

        trainClassRepo.save(trainClass);

        return true;
    }

    // SOFT DELETE
    @Transactional 
    public boolean deleteTrainClass(
            TrainClassDeleteReqDto reqDto) throws Exception {

        TrainClass trainClass =
                trainClassRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Train class not found"
                                ));

        trainClass.setDeleted(true);
        trainClass.setReason(reqDto.getClassDeleteReason());

        trainClassRepo.save(trainClass);

        return true;
    }

    // PERMANENT DELETE
    @Transactional 
    public boolean deleteTrainClassPermanently(
            TrainClassDeleteReqDto reqDto) throws Exception {

        TrainClass trainClass =
                trainClassRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Train class not found"
                                ));

        trainClassRepo.delete(trainClass);

        return true;
    }
}