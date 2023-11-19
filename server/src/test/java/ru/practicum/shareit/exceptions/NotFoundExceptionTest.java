package ru.practicum.shareit.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(controllers = NotFoundException.class)
public class NotFoundExceptionTest {

    @Test
    void testConstructor() {
        String message = "404 NOT FOUND";
        assertEquals(message, new NotFoundException(message).getMessage());
    }

}
