package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos;

import com.easyrailjourney.Validations.CoachValidaters.AtLeastOneNotNull;

import lombok.Data;

@Data
@AtLeastOneNotNull (
    fields={"id","trainNumber"}
)
public class TrainUpdateReqDto {

    long id;
    String trainNumber;
    String trainName;
    String fareStratergyType;
    String refundStrategyType;
    String trainType;
}
