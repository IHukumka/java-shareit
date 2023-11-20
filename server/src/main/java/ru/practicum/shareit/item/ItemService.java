package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.data.domain.Pageable;

import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

public interface ItemService {

    ItemDto create(Long userId, ItemDto itemDto);

    CommentDto createComment(long itemId, long userId, CommentDto commentDto);

    void delete(Long id);

    ItemDto edit(Long id, ItemDto itemDto, Long userId);

    ItemDto get(Long id, Long userId);

    List<ItemDto> getAll(Long userId, Pageable pageable);

    List<ItemDto> getRequestItems(long requestId);

    List<ItemDto> searchForItems(String text, Integer from, Integer size);

}
