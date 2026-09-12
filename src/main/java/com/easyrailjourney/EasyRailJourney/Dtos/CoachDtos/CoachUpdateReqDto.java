package com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos;

import com.easyrailjourney.Validations.CoachValidaters.AtLeastOneNotNull;

import lombok.Data;

@Data
@AtLeastOneNotNull(
    fields={"id","coachNumber"}
)
public class CoachUpdateReqDto {

    private Long id;
    private String coachNumber;
   
}