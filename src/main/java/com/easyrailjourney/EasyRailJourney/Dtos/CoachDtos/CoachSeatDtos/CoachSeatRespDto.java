package com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachSeatDtos;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoachSeatRespDto {

    private Long coachId;

    private List<Long> seatIds;

    private String message;
}