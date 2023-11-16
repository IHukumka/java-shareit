package ru.practicum.shareit.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(controllers = ExceptionRestControllerAdvice.class)
public class ExceptionsRestControllerAdviceTest {

    public ExceptionRestControllerAdvice controller = new ExceptionRestControllerAdvice();

    @Test
    void handleNotFoundExceptionTest() throws IOException {
        assertEquals("404 NOT_FOUND",
                controller.handleNotFoundException(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Test
    void handlerBadValidationTest() throws IOException {
        assertEquals("409 CONFLICT", controller.handlerBadValidation(new ResponseStatusException(HttpStatus.CONFLICT)));
    }

    @Test
    void handleInvalidParameterException() {
        assertEquals(HttpStatus.BAD_REQUEST,
                controller.handleInvalidParameterException(new IllegalArgumentException()).getStatusCode());
    }
}
