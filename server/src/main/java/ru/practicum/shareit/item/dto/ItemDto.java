package ru.practicum.shareit.item.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoL;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@Builder
@AllArgsConstructor
public class ItemDto {

    private long id;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 512)
    private String description;

    @NotNull
    private Boolean available;

    private UserDto user;

    private Long requestId;

    private BookingDtoL lastBooking;

    private BookingDtoL nextBooking;

    private List<CommentDto> comments;
}
