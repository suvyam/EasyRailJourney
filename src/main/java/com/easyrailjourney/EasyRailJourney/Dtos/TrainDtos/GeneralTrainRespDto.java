package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;

import lombok.Data;

@Data
public class GeneralTrainRespDto {
    List<Train> trains = new ArrayList<>();
    ResponseStatus responseStatus;
    String message;
}
