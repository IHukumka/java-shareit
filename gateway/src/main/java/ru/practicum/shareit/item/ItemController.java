package ru.practicum.shareit.item;

import javax.validation.Valid;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {

	private static final String HEADER_USER_ID = "X-Sharer-User-Id";
	private final ItemClient client;

	@PostMapping
	@ResponseBody
	public ResponseEntity<Object> create(@RequestBody @Valid ItemDto itemDto,
			@RequestHeader(HEADER_USER_ID) long userId) {
		log.info("Получен запрос к эндпоинту: 'POST_ITEMS'. ");
		return this.client.create(userId, itemDto);
	}

	@PostMapping("/{itemId}/comment")
	public ResponseEntity<Object> createComment(@PathVariable long itemId,
			@RequestHeader(HEADER_USER_ID) long userId,
			@RequestBody @Valid CommentDto commentDto) {
		return client.createComment(userId, itemId, commentDto);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseBody
	public void delete(@PathVariable long id) {
		log.info("Получен запрос к эндпоинту: 'DELETE_ITEMS_ID'.");
		this.client.delete(id);
	}

	@GetMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Object> get(@PathVariable long id,
			@RequestHeader(HEADER_USER_ID) long userId) {
		log.info("Получен запрос к эндпоинту: 'GET_ITEMS_ID'.");
		return this.client.get(userId, id);
	}

	@GetMapping
	public ResponseEntity<Object> getAllOwnerItems(
			@RequestHeader(HEADER_USER_ID) long userId,
			@RequestParam(required = false, defaultValue = "0") int from,
			@RequestParam(required = false, defaultValue = "10") int size) {
		return client.getAll(userId, from, size);
	}

	@GetMapping("/search")
	public ResponseEntity<Object> search(@RequestParam String text,
			@RequestParam(required = false, defaultValue = "0") int from,
			@RequestParam(required = false, defaultValue = "20") int size) {
		log.info("Получен запрос к эндпоинту: 'GET_ITEMS_SEARCH'.");
		return client.search(text, from, size);
	}

	@PatchMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Object> update(@PathVariable long id,
			@RequestBody ItemDto itemDto,
			@RequestHeader(HEADER_USER_ID) long userId) {
		log.info("Получен запрос к эндпоинту: 'PATCH_ITEMS'.");
		return this.client.edit(userId, id, itemDto);
	}

}
