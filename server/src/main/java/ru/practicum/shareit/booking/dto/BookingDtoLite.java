package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Booking.BookingStatus;

@Data
@Builder
public class BookingDtoLite {
	private long id;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime start;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime end;
	private long itemId;
	private long bookerId;
	private BookingStatus status;
}
