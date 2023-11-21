package ru.practicum.shareit.booking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking.BookingState;
import ru.practicum.shareit.booking.model.Booking.BookingStatus;

@WebMvcTest(controllers = Booking.class)
public class BookingTest {

    @Test
    void bookingStateTest() {
        assertEquals(BookingState.ALL, BookingState.fromName("ALL"));
        assertEquals(BookingState.CURRENT, BookingState.fromName("CURRENT"));
        assertEquals(BookingState.FUTURE, BookingState.fromName("FUTURE"));
        assertEquals(BookingState.PAST, BookingState.fromName("PAST"));
        assertEquals(BookingState.REJECTED, BookingState.fromName("REJECTED"));
        assertEquals(BookingState.WAITING, BookingState.fromName("WAITING"));
        assertThrows(UnsupportedOperationException.class, () -> BookingState.fromName(""));

    }

    @Test
    void bookingStatusTest() {
        assertEquals(BookingStatus.CANCELED, BookingStatus.fromName("CANCELED"));
        assertEquals(BookingStatus.APPROVED, BookingStatus.fromName("APPROVED"));
        assertEquals(BookingStatus.REJECTED, BookingStatus.fromName("REJECTED"));
        assertEquals(BookingStatus.WAITING, BookingStatus.fromName("WAITING"));
        assertThrows(UnsupportedOperationException.class, () -> BookingStatus.fromName(""));

    }

}
