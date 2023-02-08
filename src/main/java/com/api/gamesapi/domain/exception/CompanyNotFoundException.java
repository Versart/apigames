package com.api.gamesapi.domain.exception;

public class CompanyNotFoundException extends NotFoundException{

    private static final long serialVersionUID = 1l;

    public CompanyNotFoundException(String message) {
        super(message);
    }
}
