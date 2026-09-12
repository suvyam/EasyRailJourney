package com.easyrailjourney.EasyRailJourney.models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Ticket extends BaseModel {

    @OneToMany(mappedBy = "ticket")
    @JsonManagedReference
    private List<TicketPassenger> ticketPassenger;

    @NotBlank(message = "PNR is required")
    @Column(nullable = false, unique = true)
    private String pnr;

    @NotNull(message = "Issued date is required")
    @Column(nullable = false)
    private Date issuedAt;

    @NotNull(message = "Seat count is required")
    @Column(nullable = false)
    private Integer seatCount;
}