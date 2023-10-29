package ru.practicum.shareit.request;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import ru.practicum.shareit.request.dto.ItemRequestDto;

@Component
public class ItemRequestMapper {

	public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
		LocalDateTime created = itemRequest.getCreated();
		return ItemRequestDto.builder().id(itemRequest.getId()).description(itemRequest.getDescription())
				.created(created).build();
	}

	public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
		ItemRequest itemRequest = new ItemRequest();
		itemRequest.setDescription(itemRequestDto.getDescription());
		return itemRequest;
	}
}