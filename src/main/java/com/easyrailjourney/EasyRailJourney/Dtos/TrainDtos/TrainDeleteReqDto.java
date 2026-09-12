package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos;

import com.easyrailjourney.Validations.CoachValidaters.AtLeastOneNotNull;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@AtLeastOneNotNull (
    fields={"id","trainNumber"}
)

public class TrainDeleteReqDto {

    Long id;

    String trainNumber;

    @NotBlank(message = "Train deletion reason is required.")
    String trainDeleteReason;
}