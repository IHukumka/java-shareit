package ru.practicum.shareit.booking;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
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

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoL;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

	private static final String HEADER_USER_ID = "X-Sharer-User-Id";
	private static final String HEADER_ITEM_ID = "X-Sharer-Item-Id";
	private final BookingService service;

	@PostMapping
	public ResponseEntity<BookingDto> createBooking(@RequestHeader(HEADER_USER_ID) Long userId,
			@RequestBody @Valid BookingDtoL bookingDto) {
		return ResponseEntity.ok(service.add(userId, bookingDto));
	}

	@GetMapping(value = "/{bookingId}")
	public ResponseEntity<BookingDto> getBookingById(@RequestHeader(HEADER_USER_ID) Long userId,
			@PathVariable Long bookingId) {
		return ResponseEntity.ok(service.get(userId, bookingId));
	}

	@GetMapping("/item")
	public ResponseEntity<List<BookingDto>> getItemBookings(@RequestHeader(HEADER_ITEM_ID) Long itemId,
			@RequestParam(required = false, defaultValue = "ALL") String state,
			@RequestParam(required = false, defaultValue = "0") Integer from,
			@RequestParam(required = false, defaultValue = "10") Integer size) {
		return ResponseEntity.ok(service.getBookingsByItem(itemId, PageRequest.of((from / size), size)));
	}

	@GetMapping("/owner")
	public ResponseEntity<List<BookingDto>> getOwnerBookings(@RequestHeader(HEADER_USER_ID) Long userId,
			@RequestParam(required = false, defaultValue = "ALL") String state,
			@RequestParam(required = false, defaultValue = "0") Integer from,
			@RequestParam(required = false, defaultValue = "10") Integer size) {
		return ResponseEntity.ok(service.getBookingsByOwner(userId, state, PageRequest.of((from / size), size)));
	}

	@GetMapping
	public ResponseEntity<List<BookingDto>> getUserBookings(@RequestHeader(HEADER_USER_ID) Long userId,
			@RequestParam(required = false, defaultValue = "ALL") String state,
			@RequestParam(required = false, defaultValue = "0") Integer from,
			@RequestParam(required = false, defaultValue = "10") Integer size) {
		return ResponseEntity.ok(service.getBookingsByBooker(userId, state, PageRequest.of((from / size), size)));
	}

	@PatchMapping(value = "/{bookingId}")
	public ResponseEntity<BookingDto> updateBooking(@PathVariable Long bookingId, @RequestParam Boolean approved,
			@RequestHeader(HEADER_USER_ID) Long userId) {
		return ResponseEntity.ok(service.patch(bookingId, userId, approved));
	}
}