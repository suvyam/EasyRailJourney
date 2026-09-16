package com.easyrailjourney.EasyRailJourney.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.StatesDtos.GeneralStateRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.StatesDtos.StateCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.StatesDtos.StateDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.StatesDtos.StateUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.services.StateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/state")
public class StateController {

    private final StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @PostMapping
     @PreAuthorize ("hasAuthority('ADD_STATE')")
    public ResponseEntity<GeneralStateRespDto> createState(
            @Valid @RequestBody StateCreateReqDto dto) {

        GeneralStateRespDto response = new GeneralStateRespDto();

        try {
            response.setState(stateService.createState(dto));
            response.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize ("hasRole('READ_STATE')")
    public ResponseEntity<GeneralStateRespDto> getAllStates() {

        GeneralStateRespDto response = new GeneralStateRespDto();

        try {
            response.setStates(stateService.getAllStates());
            response.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize ("hasRole('READ_STATE')")
    public ResponseEntity<GeneralStateRespDto> getState(
            @PathVariable Long id) {

        GeneralStateRespDto response = new GeneralStateRespDto();

        try {
            response.setState(stateService.getStateById(id));
            response.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize ("hasRole('UPDATE_STATE')")
    public ResponseEntity<GeneralStateRespDto> updateState(
            @Valid @RequestBody StateUpdateReqDto dto) {

        GeneralStateRespDto response = new GeneralStateRespDto();

        try {
            response.setState(stateService.updateState(dto));
            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("State updated successfully");

        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @PreAuthorize ("hasRole('DELETE_STATE')")
    public ResponseEntity<GeneralStateRespDto> deleteState(
            @Valid @RequestBody StateDeleteReqDto dto) {

        GeneralStateRespDto response = new GeneralStateRespDto();

        try {
            stateService.deleteState(dto);

            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("State deleted successfully");

        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-permanently/{id}")
    @PreAuthorize ("hasRole('DELETE_STATE')")
    public ResponseEntity<GeneralStateRespDto> deleteStatePermanently(
            @PathVariable Long id) {

        GeneralStateRespDto response = new GeneralStateRespDto();

        try {
            stateService.deleteStatePermanently(id);

            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("State permanently deleted");

        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}