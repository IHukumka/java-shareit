package ru.practicum.shareit.item.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoLite;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@Builder
@AllArgsConstructor
public class ItemDto {

	private long id;

	private String name;

	private String description;

	private Boolean available;

	private UserDto user;

	private Long requestId;

	private BookingDtoLite lastBooking;

	private BookingDtoLite nextBooking;

	private List<CommentDto> comments;
}
