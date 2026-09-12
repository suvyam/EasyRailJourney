package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrainCreateReqDto {

    @NotBlank(message = "Train number is required.")
    String trainNumber;

    @NotBlank(message = "Train name is required.")
    String trainName;

    @NotBlank(message = "Train type required ")// fast express
    String trainType; // make method for create update dlete 

    String fareStratergyType;

    String refundStrategyType;

}