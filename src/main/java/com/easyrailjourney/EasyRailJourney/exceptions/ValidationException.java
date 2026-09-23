package com.easyrailjourney.EasyRailJourney.exceptions;



public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}