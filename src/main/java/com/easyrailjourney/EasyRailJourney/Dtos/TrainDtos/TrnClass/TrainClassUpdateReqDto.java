package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrnClass;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrainClassUpdateReqDto {

    @NotNull(message = "Train class ID is required.")
    private Long id;

    private String classCode;

    private String className;

    private String description;
}