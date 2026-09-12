package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.TrnClass;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrainClassCreateReqDto {

    @NotBlank(message = "Class code is required.")
    private String classCode;

    @NotBlank(message = "Class name is required.")
    private String className;

    private String description;
}