package com.easyrailjourney.EasyRailJourney.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.TicketDtos.TicketResponseDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.Ticket;
import com.easyrailjourney.EasyRailJourney.services.TicketService;


@RestController 
@RequestMapping ("/ticket")
public class TicketController {

    private TicketService ticketService;
    TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<TicketResponseDto>  syncTicket( @PathVariable (name = "id") Long bookingId){

        TicketResponseDto responseDto = new TicketResponseDto();
        try {
            Ticket  ticket = ticketService.syncTicket(bookingId);

            responseDto.setTicket(ticket);
            responseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {
            responseDto.setStatus(ResponseStatus.FAILURE);

            StringBuilder sb = new StringBuilder();
            
            for(Throwable t : e.getSuppressed()){
               sb.append("FAILURE REASON : "+ t.getMessage() + System.lineSeparator());
            }

            responseDto.setMessage(sb.toString());
        };


        return ResponseEntity.status(201).body(responseDto);
      
    }

    @GetMapping("/{pnr}")
    public ResponseEntity<TicketResponseDto>  getTicket( @PathVariable (name = "pnr") String pnr){

        TicketResponseDto responseDto = new TicketResponseDto();
        try {
            Ticket  ticket = ticketService.getTicketByPnr(pnr);

            responseDto.setTicket(ticket);
            responseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            e.printStackTrace();
        
            responseDto.setMessage(
                    e.getMessage() != null
                            ? e.getMessage()
                            : e.getClass().getSimpleName()
            );
        
            responseDto.setStatus(
                    ResponseStatus.FAILURE
            );
        
            responseDto.setTicket(
                    null
            );
        
            return ResponseEntity
                    .badRequest()
                    .body(responseDto);
        }


        return ResponseEntity.status(201).body(responseDto);
      
    }

}
