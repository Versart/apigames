package com.api.gamesapi.api.exceptionhandler;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class Problem {

    private Integer statusCode;

    private String message;

    private OffsetDateTime dateTime;
}
