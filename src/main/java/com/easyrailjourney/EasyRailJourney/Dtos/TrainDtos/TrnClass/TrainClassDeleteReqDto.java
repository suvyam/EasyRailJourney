package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrnClass;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrainClassDeleteReqDto {

    @NotNull(message = "Train class ID is required.")
    private Long id;

    @NotBlank(message = "Class deletion reason is required.")
    private String classDeleteReason;
}