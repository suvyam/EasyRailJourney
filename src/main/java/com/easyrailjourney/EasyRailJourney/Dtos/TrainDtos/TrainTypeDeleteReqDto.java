package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class TrainTypeDeleteReqDto {

    @NotNull (message="TrainType Id required")
    Long id;
    
}
