package ru.practicum.shareit.item.model;

import org.mapstruct.Mapper;

import ru.practicum.shareit.item.dto.ItemDto;

@Mapper(componentModel = "spring")
public interface ItemMapper {
	ItemDto toItemDto(Item item);

	Item toItem(ItemDto itemDto);
}
