package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos;

import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainType;

import lombok.Data;

@Data
public class GeneralTrainTypeRespDto {

    private List<TrainType> trainTypes;

    private String message;

    private ResponseStatus responseStatus;

}