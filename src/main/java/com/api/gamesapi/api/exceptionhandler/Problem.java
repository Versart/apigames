package com.api.gamesapi.api.exceptionhandler;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class Problem {

    private Integer statusCode;

    private String message;

    private OffsetDateTime dateTime;

    private List<Input> inputs;
}
