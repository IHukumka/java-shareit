package ru.practicum.shareit.booking.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.service.BookingTestData.createdDto;
import static ru.practicum.shareit.booking.service.BookingTestData.createdWrongDto;
import static ru.practicum.shareit.item.service.ItemTestData.itemDto1;
import static ru.practicum.shareit.user.service.UserTestData.userDto1;
import static ru.practicum.shareit.user.service.UserTestData.userDto2;
import static ru.practicum.shareit.user.service.UserTestData.userDto3;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Booking.BookingStatus;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceImplTest {
				@Autowired
				private final BookingService bookingService;
				@Autowired
				private final ItemService itemService;
				@Autowired
				private final UserService userService;
				@Autowired
				private final BookingStorage bookingStorage;
				UserDto bookerDto;
				ItemDto itemDto;

				@BeforeEach
				void setUp() {
								userService.create(userDto1);
								bookerDto = userService.create(userDto2);
								userService.create(userDto3);
								itemDto = itemService.create(1L, itemDto1);
								bookingService.add(2L, BookingTestData.BookingDtoL1);
				}

				@Test
				void addTest() {
								bookingService.add(2L, createdDto);
								List<BookingDto> bookings = bookingService.getBookingsByBooker(2L, "WAITING", PageRequest.of(0, 10));
								assertThat(bookings.size(), equalTo(2));
				}

				@Test
				void addWrongDateTest() {
								RuntimeException ex = Assertions.assertThrows(ResponseStatusException.class,
																() -> bookingService.add(2L, createdWrongDto));
								assertEquals("400 BAD_REQUEST", ex.getMessage());
				}

				@Test
				void updateTest() {
								bookingService.update(1L, 1L, false);
								List<BookingDto> userBookings = bookingService.getBookingsByBooker(2L, "REJECTED", PageRequest.of(0, 10));
								assertThat(userBookings.size(), equalTo(1));
								List<BookingDto> ownerBookings = bookingService.getBookingsByOwner(1L, "REJECTED", PageRequest.of(0, 10));
								assertThat(ownerBookings.size(), equalTo(1));
				}

				@Test
				void updateWrongUserTest() {
								assertThrows(ResponseStatusException.class, () -> bookingService.update(1L, 2L, false));
				}

				@Test
				void updateAlreadyApprovedTest() {
								bookingService.update(1L, 1L, true);
								assertThrows(ResponseStatusException.class, () -> bookingService.update(1L, 1L, true));
				}

				@Test
				void getBookingById() {
								bookingService.add(2L, createdDto);
								List<BookingDto> bookings = bookingService.getBookingsByBooker(2L, "ALL", PageRequest.of(0, 10));
								assertThat(bookings.size(), equalTo(2));
				}

				@Test
				void getBookingByWrongBookingIdTest() {
								assertThrows(ResponseStatusException.class, () -> bookingService.get(2L, 10L));
				}

				@Test
				void getBookingByWrongUserIdTest() {
								assertThrows(ResponseStatusException.class, () -> bookingService.get(3L, 1L));
				}

				@Test
				void getBookingsByBookerTest() {
								bookingService.add(2L, createdDto);
								List<BookingDto> bookings = bookingService.getBookingsByBooker(2L, "ALL", PageRequest.of(0, 10));
								assertThat(bookings.get(0).getEnd().toString(), equalTo(createdDto.getEnd().toString()));
				}

				@Test
				void getWrongStatusUserBookingsTest() {
								String state = "WRONG";
								RuntimeException ex = Assertions.assertThrows(IllegalArgumentException.class,
																() -> bookingService.getBookingsByBooker(2L, state, PageRequest.of(0, 10)));
								assertEquals("Unknown state: " + state, ex.getMessage());
				}

				@Test
				void getDiffStatusUserBookingsTest() {
								bookingService.add(2L, createdDto);
								List<BookingDto> waitingBookings = bookingService.getBookingsByBooker(2L, "WAITING", PageRequest.of(0, 10));
								assertThat(waitingBookings.size(), equalTo(2));

								bookingService.update(2L, 1L, true);
								List<BookingDto> futureBookings = bookingService.getBookingsByBooker(2L, "FUTURE", PageRequest.of(0, 10));
								assertThat(futureBookings.size(), equalTo(2));

								bookingService.add(2L, BookingTestData.BookingDtoL2);
								bookingService.update(3L, 1L, true);
								List<BookingDto> currentBookings = bookingService.getBookingsByBooker(2L, "CURRENT", PageRequest.of(0, 10));
								assertThat(currentBookings.size(), equalTo(1));
				}

				@Test
				void getPastStatusUserAndOwnerBookingsTest() {
								Booking booking = Booking.builder().start(LocalDateTime.of(2022, 8, 1, 12, 15, 1))
																.end(LocalDateTime.of(2022, 8, 2, 12, 15, 1)).item(ItemMapper.toItem(itemDto))
																.booker(UserMapper.toUser(bookerDto)).status(BookingStatus.APPROVED).build();
								bookingStorage.save(booking);
								List<BookingDto> pastUserBookings = bookingService.getBookingsByBooker(2, "PAST", PageRequest.of(0, 10));
								assertThat(pastUserBookings.size(), equalTo(1));
								List<BookingDto> pastOwnerBookings = bookingService.getBookingsByOwner(1, "PAST", PageRequest.of(0, 10));
								assertThat(pastOwnerBookings.size(), equalTo(1));
				}

				@Test
				void getBookingsByOwnerTest() {
								List<BookingDto> bookings = bookingService.getBookingsByOwner(1L, "ALL", PageRequest.of(0, 10));
								assertThat(bookings.size(), equalTo(1));
				}

				@Test
				void getWrongStatusOwnerBookingsTest() {
								String state = "WRONG";
								RuntimeException ex = Assertions.assertThrows(IllegalArgumentException.class,
																() -> bookingService.getBookingsByOwner(2L, state, PageRequest.of(0, 10)));
								assertEquals("Unknown state: " + state, ex.getMessage());
				}

				@Test
				void getDiffStatusOwnerBookingsTest() {
								bookingService.add(2L, createdDto);
								List<BookingDto> waitingBookings = bookingService.getBookingsByOwner(1L, "WAITING", PageRequest.of(0, 10));
								assertThat(waitingBookings.size(), equalTo(2));

								bookingService.update(2L, 1L, true);
								List<BookingDto> futureBookings = bookingService.getBookingsByOwner(1L, "FUTURE", PageRequest.of(0, 10));
								assertThat(futureBookings.size(), equalTo(2));

								bookingService.add(2L, BookingTestData.BookingDtoL2);
								bookingService.update(3L, 1L, true);
								List<BookingDto> currentBookings = bookingService.getBookingsByOwner(1, "CURRENT", PageRequest.of(0, 10));
								assertThat(currentBookings.size(), equalTo(1));
				}
}