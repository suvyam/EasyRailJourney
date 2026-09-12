package com.easyrailjourney.EasyRailJourney.Dtos.CoachDtos.CoachSeatDtos;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignCoachSeatReqDto {

    @NotNull(message = "Coach ID is required")
    private Long coachId;

    @NotEmpty(message = "Seat IDs cannot be empty")
    private List<@NotNull(message = "Seat ID cannot be null") Long> seatIds;
}