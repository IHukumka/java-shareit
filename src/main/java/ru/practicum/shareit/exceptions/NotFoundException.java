package ru.practicum.shareit.exceptions;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }
}