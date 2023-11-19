package ru.practicum.shareit.exceptions;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponseMessage {

    private Instant time;
    private int status;
    private String error;
    private String exception;
    private String message;
}
