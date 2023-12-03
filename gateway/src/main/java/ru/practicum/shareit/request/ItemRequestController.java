package ru.practicum.shareit.request;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Validated
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
	private final ItemRequestClient client;

	private static final String HEADER_USER_ID = "X-Sharer-User-Id";

	@GetMapping
	public ResponseEntity<Object> getAllRequests(
			@RequestHeader(HEADER_USER_ID) long userId) {
		return client.getAllRequests(userId);
	}

	@GetMapping("/{requestId}")
	public ResponseEntity<Object> getRequest(
			@RequestHeader(HEADER_USER_ID) long userId,
			@PathVariable long requestId) {
		return client.getRequest(userId, requestId);
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getAllUsersRequests(
			@RequestHeader(HEADER_USER_ID) long userId,
			@RequestParam(required = false, defaultValue = "0") int from,
			@RequestParam(required = false, defaultValue = "10") int size) {
		if (from < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		return client.getAllUsersRequests(userId, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> createRequest(
			@RequestHeader(HEADER_USER_ID) long userId,
			@RequestBody @Valid ItemRequestDto itemRequestDto) {
		return client.createRequest(userId, itemRequestDto);
	}
}
