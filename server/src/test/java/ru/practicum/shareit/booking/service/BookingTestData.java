package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;

import ru.practicum.shareit.booking.dto.BookingDtoLite;

public class BookingTestData {
    public static final BookingDtoLite BookingDtoL1 = BookingDtoLite.builder().start(LocalDateTime.now().plusDays(1))
            .end(LocalDateTime.now().plusDays(2)).itemId(1L).build();

    public static final BookingDtoLite BookingDtoL2 = BookingDtoLite.builder().start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(1)).itemId(1L).build();

    public static final BookingDtoLite createdDto = BookingDtoLite.builder().start(LocalDateTime.now().plusDays(5))
            .end(LocalDateTime.now().plusDays(6)).itemId(1L).build();
    public static final BookingDtoLite createdWrongDto = BookingDtoLite.builder().start(LocalDateTime.now().plusDays(5))
            .end(LocalDateTime.now().plusDays(3)).itemId(1L).build();
}