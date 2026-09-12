package com.easyrailjourney.Validations.SeatValidaters;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Seats.SeatDeleteReqDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SeatValidate implements  ConstraintValidator<ValidSeat, SeatDeleteReqDto> {

    @Override
    public boolean isValid(SeatDeleteReqDto dto, ConstraintValidatorContext context) {


        if(dto==null)return true;

        if(dto.getId()!=null){
            return true;
        };

         // Case 2: Delete using coachId + seatNumber
         return dto.getCoachId() != null
         && dto.getSeatNumber() != null
         && !dto.getSeatNumber().isBlank();
      

    }



    
}
