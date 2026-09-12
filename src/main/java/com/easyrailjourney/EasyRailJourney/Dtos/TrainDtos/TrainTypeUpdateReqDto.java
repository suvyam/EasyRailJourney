package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class TrainTypeUpdateReqDto {

    @NotNull (message="TrainType Id required")
    Long id;

    String name;
    
}
