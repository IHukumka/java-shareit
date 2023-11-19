package ru.practicum.shareit.item;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemService service;

    @PostMapping
    @ResponseBody
    public ResponseEntity<ItemDto> create(@RequestBody @Valid ItemDto itemDto,
            @RequestHeader(HEADER_USER_ID) Long userId) {
        log.info("Получен запрос к эндпоинту: 'POST_ITEMS'. ");
        return ResponseEntity.ok(this.service.create(userId, itemDto));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long itemId,
            @RequestHeader(HEADER_USER_ID) Long userId, @RequestBody @Valid CommentDto commentDto) {
        return ResponseEntity.ok(service.createComment(itemId, userId, commentDto));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        log.info("Получен запрос к эндпоинту: 'DELETE_ITEMS_ID'.");
        this.service.delete(id);
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<ItemDto> get(@PathVariable Long id, @RequestHeader(HEADER_USER_ID) Long userId) {
        log.info("Получен запрос к эндпоинту: 'GET_ITEMS_ID'.");
        return ResponseEntity.ok(this.service.get(id, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllOwnerItems(@RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getAll(userId, PageRequest.of((from / size), size)));
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        log.info("Получен запрос к эндпоинту: 'GET_ITEMS_SEARCH'.");
        return this.service.searchForItems(text, from, size);
    }

    @PatchMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<ItemDto> update(@PathVariable Long id, @RequestBody ItemDto itemDto,
            @RequestHeader(HEADER_USER_ID) Long userId) {
        log.info("Получен запрос к эндпоинту: 'PATCH_ITEMS'.");
        return ResponseEntity.ok(this.service.edit(id, itemDto, userId));
    }

}
