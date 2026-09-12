package com.easyrailjourney.EasyRailJourney.Dtos.Fares;

import lombok.Data;

@Data 
public class FareRuleRespDto {


    private String trainName;

    private String stateName;

    private String classType;

    private String calculationType;

    private Double value;

    private Integer priority;

    private boolean active = true;

    Boolean isDeleted;
    
}
