package com.api.gamesapi.domain.exception;

public class GameNotFoundException extends RuntimeException{

    public GameNotFoundException(String message) {
        super(message);
    }
}
