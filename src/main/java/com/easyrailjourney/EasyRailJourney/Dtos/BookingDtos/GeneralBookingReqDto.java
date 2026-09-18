package com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos;

import java.util.Date;

import lombok.Data;

@Data
public class GeneralBookingReqDto { // delete,update or for validate quierys 

   Long booking_id;

   String pnr;

   Long schedule_train_id;

   Long srcStation;

   Long dstStation;

   String trainClass;

   Integer numberOfSeats;

   Long scheduleTrainClassId;

   Date journeyDate;




}
