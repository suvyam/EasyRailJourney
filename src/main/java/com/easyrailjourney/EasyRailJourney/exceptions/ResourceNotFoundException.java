package com.easyrailjourney.EasyRailJourney.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}