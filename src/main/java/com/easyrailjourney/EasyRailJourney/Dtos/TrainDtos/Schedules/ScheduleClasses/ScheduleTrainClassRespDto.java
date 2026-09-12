package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.SeatType;

import lombok.Data;

@Data 
public class ScheduleTrainClassRespDto {


      String scheduleTrainName;
      String scheduleTrainNumber;

      String ClassName;
      
      Set<String> coacheNames = new HashSet<>();

      Map<SeatType, List<String>> scheduleTrainClassSeats = new HashMap<>();

      Boolean isDeleted;
}
