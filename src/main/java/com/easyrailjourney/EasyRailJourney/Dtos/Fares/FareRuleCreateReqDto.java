package com.easyrailjourney.EasyRailJourney.Dtos.Fares;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FareRuleCreateReqDto {



    Long trainId;

    Long stateId;

    @NotBlank
    String className;

    @NotBlank
    String calculationType;

    @NotNull
    Double value;

    @NotNull
    Integer priority;

    @NotNull
    Boolean active;

    @NotNull
    Double baseFare;

    Boolean isDeleted;


}
