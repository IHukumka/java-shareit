package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoL;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

public interface BookingMapper {

				static Booking toBooking(BookingDto bookingDto) {
								return Booking.builder().id(bookingDto.getId()).start(bookingDto.getStart()).end(bookingDto.getEnd())
																.item(ItemMapper.toItem(bookingDto.getItem())).booker(UserMapper.toUser(bookingDto.getBooker()))
																.status(bookingDto.getStatus()).build();
				}

				static Booking toBooking(BookingDtoL bookingDto) {
								return Booking.builder().id(bookingDto.getId()).start(bookingDto.getStart()).end(bookingDto.getEnd())
																.status(bookingDto.getStatus()).build();
				}

				static BookingDto toBookingDto(Booking booking) {
								return BookingDto.builder().id(booking.getId()).start(booking.getStart()).end(booking.getEnd())
																.item(ItemMapper.toItemDto(booking.getItem())).booker(UserMapper.toUserDto(booking.getBooker()))
																.status(booking.getStatus()).build();
				}

				static BookingDtoL toBookingDtoL(Booking booking) {
								return BookingDtoL.builder().id(booking.getId()).start(booking.getStart()).end(booking.getEnd())
																.itemId(booking.getItem().getId()).bookerId(booking.getBooker().getId()).status(booking.getStatus())
																.build();
				}

}
