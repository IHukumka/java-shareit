package ru.practicum.shareit.item.model;

import org.springframework.context.annotation.Lazy;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.UserMapper;

@Lazy
public interface ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder().id(item.getId()).name(item.getName()).description(item.getDescription())
                .available(item.getAvailable()).requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .user(UserMapper.toUserDto(item.getUser())).build();
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = new ItemRequest();
            itemRequest.setId(itemDto.getRequestId());
            item.setRequest(itemRequest);
        }
        item.setUser(UserMapper.toUser(itemDto.getUser()));
        return item;
    }

}
