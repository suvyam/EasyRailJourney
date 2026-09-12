package com.easyrailjourney.EasyRailJourney.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data 
@Entity 
public class TicketPassenger  extends  BaseModel{


    @ManyToOne 
    @JoinColumn (name = "ticket_id", nullable=false)
    @JsonBackReference 
    Ticket ticket;

    @NotBlank (message = "Passnger name required for booking Resp")
    @Column(nullable=false)
    String name;

    @NotBlank(message = "Passnger age required for booking Resp")
    @Column(nullable=false)
    String age;

    @NotBlank(message = "Seat Number required for booking Resp")
    @Column(nullable=false)
    String seatNumber;

    @NotBlank(message = "Class name required for booking Resp")
    @Column(nullable=false)
    String className;

    @NotBlank(message = "Coach number required for booking Resp")
    @Column(nullable=false)
    String coachNumber;


    @NotBlank(message = "Train name required for booking Resp")
    @Column(nullable=false)
    String trainName;

    @NotBlank(message = "Train number required for booking Resp")
    @Column(nullable=false)
    String trainNumber;

    @NotNull(message = "arrival time required for booking Resp")
    @Column(nullable=false)
    Date arrivalTime;

    @NotNull(message = "departure rime required for booking Resp")
    @Column(nullable=false)
    Date departureTime;

    @NotBlank(message = "arrival station name required for booking Resp")
    @Column(nullable=false)
    String arrivalStationName;

    @NotBlank(message = "departure station name required for booking Resp")
    @Column(nullable=false)
    String departureStationName;

    @NotBlank(message = "booking status required for booking Resp")
    @Column(nullable=false)
    String bookingStatus;
}
