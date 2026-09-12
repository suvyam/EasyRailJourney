package com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos;


import com.easyrailjourney.Validations.CoachValidaters.AtLeastOneNotNull;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@AtLeastOneNotNull(
    fields={"id","coachNumber"}
)
public class CoachDeleteReqDto {

    
    private Long id;
    private String coachNumber;

    @NotBlank(message = "Coach deletion reason is required.")
    private String coachDeleteReason;
}