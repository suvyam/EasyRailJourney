package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class TrainTypeReqDto{

    @NotBlank(message="Train type name required")
    String name;
    
}