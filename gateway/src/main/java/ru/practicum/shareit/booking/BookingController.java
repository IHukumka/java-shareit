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
import ru.practicum.shareit.booking.dto.BookingDtoL;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private static final String HEADER_ITEM_ID = "X-Sharer-Item-Id";
    private final BookingClient client;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(HEADER_USER_ID) Long userId,
            @RequestBody @Valid BookingDtoL bookingDto) {
        return client.add(userId, bookingDto);
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long bookingId) {
        return client.get(userId, bookingId);
    }

    @GetMapping("/item")
    public ResponseEntity<Object> getItemBookings(@RequestHeader(HEADER_ITEM_ID) Long itemId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (from.longValue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return client.getBookingsByItem(itemId, state, from.intValue(), size.intValue());
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (from.longValue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return client.getBookingsByUser(userId, state, from.intValue(), size.intValue());
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (from.longValue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return client.getBookingsByBooker(userId, state, from.intValue(), size.intValue());
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> updateBooking(@PathVariable Long bookingId, @RequestParam Boolean approved,
            @RequestHeader(HEADER_USER_ID) Long userId) {
        return client.update(userId, bookingId, approved);
    }
}