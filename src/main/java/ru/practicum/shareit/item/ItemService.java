package ru.practicum.shareit.item;

import java.util.List;

import ru.practicum.shareit.item.dto.ItemDto;

public interface ItemService {

	List<ItemDto> getAll(Long userId);

	ItemDto create(Long userId, ItemDto itemDto);

	ItemDto edit(Long id, ItemDto itemDto, Long userId);

	void clearAll();

	void delete(Long id);

	ItemDto get(Long id);

	List<ItemDto> getRequestItems(long requestId);

	List<ItemDto> searchForItems(String text, Integer from, Integer size);

}
