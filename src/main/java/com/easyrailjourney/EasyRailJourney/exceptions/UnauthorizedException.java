package com.easyrailjourney.EasyRailJourney.exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}