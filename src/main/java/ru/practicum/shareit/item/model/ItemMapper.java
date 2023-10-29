package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Component;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;

@Component
public class ItemMapper {

	public ItemDto toItemDto(Item item) {
		return ItemDto.builder().id(item.getId()).name(item.getName()).description(item.getDescription())
				.available(item.getAvailable()).requestId(item.getRequest() != null ? item.getRequest().getId() : null)
				.build();
	}

	public Item toItem(ItemDto itemDto) {
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
		return item;
	}

}
