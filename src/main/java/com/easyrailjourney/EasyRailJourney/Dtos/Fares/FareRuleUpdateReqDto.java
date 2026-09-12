package com.easyrailjourney.EasyRailJourney.Dtos.Fares;

import com.easyrailjourney.Validations.CoachValidaters.AtLeastOneNotNull;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

@AtLeastOneNotNull (
    fields={"id","name"} // add message
)
public class FareRuleUpdateReqDto {



    @NotNull 
    Long id;

    Long trainId;


    Long stateId;


    String className;


    String calculationType;


    Double value;


    Integer priority;


    Boolean active;

    Boolean isDeleted;

}