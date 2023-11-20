package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;

import ru.practicum.shareit.booking.Booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoL;

public interface BookingService {

    BookingDto add(long userId, BookingDtoL bookingDto);

    BookingDto update(long bookingId, long userId, Boolean approved);

    BookingDto get(long userId, long bookingId);

    List<BookingDto> getBookingsByBooker(long userId, String state, Pageable pageable);

    List<BookingDto> getBookingsByOwner(long ownerId, String state, Pageable pageable);

    List<BookingDto> getBookingsByItem(long itemId, Pageable pageable);

    List<BookingDto> findByItem_IdAndBooker_IdAndStatusAndEndBefore(long itemId, long userId, BookingStatus status,
            LocalDateTime now, Pageable pageable);

}
