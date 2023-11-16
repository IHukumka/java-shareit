package ru.practicum.shareit.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(controllers = ValidationException.class)
public class ValidationExceptionTest {

    @Test
    void testConstructor() {
        String message = "409 CONFLICT";
        assertEquals(message, new ValidationException(message).getMessage());
    }

}
