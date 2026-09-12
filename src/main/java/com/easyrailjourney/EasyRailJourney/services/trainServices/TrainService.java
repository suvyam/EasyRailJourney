package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrainCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrainDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrainUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainType;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.TrainRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.TrainTypeRepo;
@Service
public class TrainService {

    private final TrainRepo trainRepository;
    private final TrainTypeRepo trainTypeRepo;

    public TrainService(TrainRepo trainRepository,TrainTypeRepo trainTypeRepo) {
        this.trainRepository = trainRepository;
        this.trainTypeRepo = trainTypeRepo;
    }

    // CREATE TRAIN
    public Train createTrain(TrainCreateReqDto reqDto)  throws Exception{

        Train train = new Train();

        train.setTrainNumber(reqDto.getTrainNumber());
        train.setTrainName(reqDto.getTrainName());

        String trainType = reqDto.getTrainType().toUpperCase();
        TrainType ans =  trainTypeRepo.findByName(trainType).orElseThrow(()-> new Exception("Train Type not exists")); 

        train.setTrainType(ans);

        return trainRepository.save(train);
    }

    // GET ALL TRAIN
    public List<Train> getAllTrains() {

        return trainRepository.findAll();
    }

    // SEARCH TRAIN
    public List<Train> searchTrain(
            Long id,
            String trainNumber,
            String trainName,
            String trainTypeName,
            boolean isDeleted) {
    

        Optional<TrainType> trainType =  trainTypeRepo.findByName(trainTypeName);

        return trainRepository.searchTrain(
                id,
                trainNumber,
                trainName,
                trainType.get(),
                isDeleted
        );
    }

    // UPDATE TRAIN
    public boolean updateTrain(TrainUpdateReqDto reqDto) throws Exception {

        Train train = trainRepository
                .findById(reqDto.getId())
                .orElseThrow(() ->
                        new RuntimeException("Train not found"));

        if (reqDto.getTrainNumber() != null) {
            train.setTrainNumber(reqDto.getTrainNumber());
        }

        if (reqDto.getTrainName() != null) {
            train.setTrainName(reqDto.getTrainName());
        }

        String trainType = reqDto.getTrainType().toUpperCase();
        TrainType ans =  trainTypeRepo.findByName(trainType).orElseThrow(()-> new Exception("Train Type not exists")); 

        train.setTrainType(ans);

        trainRepository.save(train);

        return true;
    }

    // SOFT DELETE TRAIN
    public boolean deleteTrain(TrainDeleteReqDto reqDto) {

        Train train;

        if (reqDto.getId() != 0) {

            train = trainRepository
                    .findByIdAndIsDeleted(
                            reqDto.getId(),
                            false
                    )
                    .orElseThrow(() ->
                            new RuntimeException("Train not found"));

        } else if (reqDto.getTrainNumber() != null) {

            train = trainRepository
                    .findByTrainNumberAndIsDeleted(
                            reqDto.getTrainNumber(),
                            false
                    )
                    .orElseThrow(() ->
                            new RuntimeException("Train not found"));

        } else {
            return false;
        }

        train.setIsDeleted(true);

        trainRepository.save(train);

        return true;
    }

    // PERMANENT DELETE TRAIN
    public boolean deleteTrainPermanently(
            TrainDeleteReqDto reqDto) {

        Train train;

        if (reqDto.getId() != 0) {

            train = trainRepository
                    .findById(reqDto.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Train not found"));

        } else if (reqDto.getTrainNumber() != null) {

            train = trainRepository
                    .findByTrainNumberAndIsDeleted(
                            reqDto.getTrainNumber(),
                            true
                    )
                    .orElseThrow(() ->
                            new RuntimeException("Train not found"));

        } else {
            return false;
        }

        trainRepository.delete(train);

        return true;
    };





}