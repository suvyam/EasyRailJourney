package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrnClass;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainClass;

import lombok.Data;

@Data
public class GeneralTrainClassRespDto {

    private List<TrainClass> trainClasses = new ArrayList<>();
    private TrainClass trainClass;
    private ResponseStatus responseStatus;
    private String message;
}