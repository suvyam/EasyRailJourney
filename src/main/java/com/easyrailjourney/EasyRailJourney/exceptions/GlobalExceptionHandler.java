package com.easyrailjourney.EasyRailJourney.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.easyrailjourney.EasyRailJourney.Dtos.ErrorResponseDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {


   

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleBookingValidation(
        ResourceNotFoundException ex) {

        ErrorResponseDto response = new ErrorResponseDto();

        response.setResponseStatus(ResponseStatus.FAILURE);
        response.setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleBookingValidation(
        ValidationException ex) {

        ErrorResponseDto response = new ErrorResponseDto();

        response.setResponseStatus(ResponseStatus.FAILURE);
        response.setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatusCode.valueOf(400))
                .body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleBookingValidation(
        ConflictException ex) {

        ErrorResponseDto response = new ErrorResponseDto();

        response.setResponseStatus(ResponseStatus.FAILURE);
        response.setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatusCode.valueOf(409))
                .body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleBookingValidation(
        UnauthorizedException ex) {

        ErrorResponseDto response = new ErrorResponseDto();

        response.setResponseStatus(ResponseStatus.FAILURE);
        response.setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatusCode.valueOf(401))
                .body(response);
    }
    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {

        ErrorResponseDto response = new ErrorResponseDto();

        response.setMessage("Internal server error"+ex.getMessage());
        response.setResponseStatus(ResponseStatus.FAILURE);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}