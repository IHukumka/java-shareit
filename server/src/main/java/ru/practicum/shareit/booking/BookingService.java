package ru.practicum.shareit.booking;

import java.util.List;

import org.springframework.data.domain.Pageable;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoLite;

public interface BookingService {

	BookingDto add(long userId, BookingDtoLite bookingDto);

	BookingDto update(long bookingId, long userId, Boolean approved);

	BookingDto get(long userId, long bookingId);

	List<BookingDto> getBookingsByBooker(long userId, String state,
			Pageable pageable);

	List<BookingDto> getBookingsByOwner(long ownerId, String state,
			Pageable pageable);

	List<BookingDto> getBookingsByItem(long itemId, Pageable pageable);

}
