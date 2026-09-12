package com.easyrailjourney.EasyRailJourney.Dtos.StatesDtos;
import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.States;

import lombok.Data;

@Data
public class GeneralStateRespDto {

    private States state;
    private List<States> states = new ArrayList<>();

    private ResponseStatus responseStatus;
    private String message;
}