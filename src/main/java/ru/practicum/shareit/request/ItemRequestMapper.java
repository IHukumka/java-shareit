package ru.practicum.shareit.request;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Lazy;

import ru.practicum.shareit.request.dto.ItemRequestDto;

@Lazy
public interface ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        LocalDateTime created = itemRequest.getCreated();
        return ItemRequestDto.builder().id(itemRequest.getId()).description(itemRequest.getDescription())
                .created(created).build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }
}