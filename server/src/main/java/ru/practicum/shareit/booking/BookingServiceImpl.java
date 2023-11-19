package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.booking.Booking.BookingState;
import ru.practicum.shareit.booking.Booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoL;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingDto add(long userId, BookingDtoL bookingDto) {

        User user = UserMapper.toUser(userService.get(userId));
        Item item = ItemMapper.toItem(itemService.get(bookingDto.getItemId(), userId));
        if (Objects.equals(item.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (end.isBefore(start) || start.equals(end) || item.getAvailable() != true) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Booking booking = Booking.builder().start(bookingDto.getStart()).end(bookingDto.getEnd()).item(item)
                .booker(user).status(BookingStatus.WAITING).build();
        return BookingMapper.toBookingDto(bookingStorage.save(booking));

    }

    @Override
    public List<BookingDto> findByItem_IdAndBooker_IdAndStatusAndEndBefore(long itemId, long userId,
            BookingStatus status, LocalDateTime now, Pageable pageable) {
        return bookingStorage.findByItem_IdAndBooker_IdAndStatusAndEndBefore(itemId, userId, status, now, pageable)
                .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto get(long userId, long bookingId) {
        userService.get(userId);
        try {
            Booking booking = bookingStorage.findById(bookingId).get();
            if (userId == booking.getBooker().getId() || userId == booking.getItem().getUser().getId()) {
                return BookingMapper.toBookingDto(booking);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<BookingDto> getBookingsByBooker(long userId, String state, Pageable pageable) {
        userService.get(userId);
        Page<Booking> userBookings;
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
            switch (bookingState) {
            case ALL:
                userBookings = bookingStorage.findByBooker_IdOrderByStartDesc(userId, pageable);
                break;
            case CURRENT:
                userBookings = bookingStorage.findByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now(), pageable);
                break;
            case PAST:
                userBookings = bookingStorage.findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                        pageable);
                break;
            case FUTURE:
                userBookings = bookingStorage.findByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(),
                        pageable);
                break;
            case WAITING:
                userBookings = bookingStorage.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING,
                        pageable);
                break;
            case REJECTED:
                userBookings = bookingStorage.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED,
                        pageable);
                break;
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }

        return userBookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByItem(long itemId, Pageable pageable) {
        Page<Booking> itemBookings = bookingStorage.findByItem_IdOrderByStartDesc(itemId, pageable);
        return itemBookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByOwner(long userId, String state, Pageable pageable) {
        userService.get(userId);
        Page<Booking> ownerBookings;
        try {
            BookingState bookingState = BookingState.valueOf(state);
            switch (bookingState) {
            case ALL:
                ownerBookings = bookingStorage.findByItem_UserIdOrderByStartDesc(userId, pageable);
                break;
            case CURRENT:
                ownerBookings = bookingStorage.findByItem_UserIdAndEndAfterAndStartBeforeOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now(), pageable);
                break;
            case PAST:
                ownerBookings = bookingStorage.findByItem_UserIdAndEndBeforeOrderByStartDesc(userId,
                        LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                ownerBookings = bookingStorage.findByItem_UserIdAndStartAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), pageable);
                break;
            case WAITING:
                ownerBookings = bookingStorage.findByItem_UserIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING,
                        pageable);
                break;
            case REJECTED:
                ownerBookings = bookingStorage.findByItem_UserIdAndStatusOrderByStartDesc(userId,
                        BookingStatus.REJECTED, pageable);
                break;
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }

        return ownerBookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto update(long bookingId, long userId, Boolean approved) {
        userService.get(userId);
        Booking booking = BookingMapper.toBooking(get(userId, bookingId));
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (userId == booking.getItem().getUser().getId()) {
            BookingStatus status = (approved) ? BookingStatus.APPROVED : BookingStatus.REJECTED;
            booking.setStatus(status);
            return BookingMapper.toBookingDto(bookingStorage.save(booking));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
