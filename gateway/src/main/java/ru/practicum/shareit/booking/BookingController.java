package ru.practicum.shareit.booking;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoLight;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

	private static final String HEADER_USER_ID = "X-Sharer-User-Id";
	private static final String HEADER_ITEM_ID = "X-Sharer-Item-Id";
	private final BookingClient client;

	@PostMapping
	public ResponseEntity<Object> createBooking(
			@RequestHeader(HEADER_USER_ID) long userId,
			@RequestBody @Valid BookingDtoLight bookingDto) {
		return client.add(userId, bookingDto);
	}

	@GetMapping(value = "/{bookingId}")
	public ResponseEntity<Object> getBookingById(
			@RequestHeader(HEADER_USER_ID) long userId,
			@PathVariable Long bookingId) {
		return client.get(userId, bookingId);
	}

	@GetMapping("/item")
	public ResponseEntity<Object> getItemBookings(
			@RequestHeader(HEADER_ITEM_ID) long itemId,
			@RequestParam(required = false, defaultValue = "ALL") String state,
			@RequestParam(required = false, defaultValue = "0") int from,
			@RequestParam(required = false, defaultValue = "10") int size) {
		if (from < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		return client.getBookingsByItem(itemId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getOwnerBookings(
			@RequestHeader(HEADER_USER_ID) long userId,
			@RequestParam(required = false, defaultValue = "ALL") String state,
			@RequestParam(required = false, defaultValue = "0") int from,
			@RequestParam(required = false, defaultValue = "10") int size) {
		if (from < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		return client.getBookingsByUser(userId, state, from, size);
	}

	@GetMapping
	public ResponseEntity<Object> getUserBookings(
			@RequestHeader(HEADER_USER_ID) long userId,
			@RequestParam(required = false, defaultValue = "ALL") String state,
			@RequestParam(required = false, defaultValue = "0") int from,
			@RequestParam(required = false, defaultValue = "10") int size) {
		if (from < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		return client.getBookingsByBooker(userId, state, from, size);
	}

	@PatchMapping(value = "/{bookingId}")
	public ResponseEntity<Object> updateBooking(@PathVariable long bookingId,
			@RequestParam Boolean approved,
			@RequestHeader(HEADER_USER_ID) long userId) {
		return client.update(userId, bookingId, approved);
	}
}