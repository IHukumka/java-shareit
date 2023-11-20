package ru.practicum.shareit.exceptions;

@SuppressWarnings("serial")
public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
    }
}