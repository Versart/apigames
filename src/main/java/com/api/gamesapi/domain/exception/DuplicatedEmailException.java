package com.api.gamesapi.domain.exception;

public class DuplicatedEmailException extends RuntimeException{

    public DuplicatedEmailException(String msg){
        super(msg);
    }
    public DuplicatedEmailException() {

    }
}
