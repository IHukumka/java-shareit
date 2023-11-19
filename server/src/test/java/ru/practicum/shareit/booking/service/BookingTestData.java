package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;

import ru.practicum.shareit.booking.dto.BookingDtoL;

public class BookingTestData {
    public static final BookingDtoL BookingDtoL1 = BookingDtoL.builder().start(LocalDateTime.now().plusDays(1))
            .end(LocalDateTime.now().plusDays(2)).itemId(1L).build();

    public static final BookingDtoL BookingDtoL2 = BookingDtoL.builder().start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(1)).itemId(1L).build();

    public static final BookingDtoL createdDto = BookingDtoL.builder().start(LocalDateTime.now().plusDays(5))
            .end(LocalDateTime.now().plusDays(6)).itemId(1L).build();
    public static final BookingDtoL createdWrongDto = BookingDtoL.builder().start(LocalDateTime.now().plusDays(5))
            .end(LocalDateTime.now().plusDays(3)).itemId(1L).build();
}