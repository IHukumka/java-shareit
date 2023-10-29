package ru.practicum.shareit.request;

import java.util.List;

import org.springframework.data.domain.PageRequest;
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

import lombok.AllArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Validated
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
	private final ItemRequestService service;
	private static final String HEADER_USER_ID = "X-Sharer-User-Id";

	@GetMapping
	public ResponseEntity<List<ItemRequestDto>> getAllRequests(@RequestHeader(HEADER_USER_ID) Long userId) {
		return ResponseEntity.ok(service.getAllRequests(userId));
	}

	@GetMapping("/{requestId}")
	public ResponseEntity<ItemRequestDto> getRequest(@RequestHeader(HEADER_USER_ID) Long userId,
			@PathVariable Long requestId) {
		return ResponseEntity.ok(service.getRequest(userId, requestId));
	}

	@GetMapping("/all")
	public ResponseEntity<List<ItemRequestDto>> getAllUsersRequests(@RequestHeader(HEADER_USER_ID) Long userId,
			@RequestParam(required = false, defaultValue = "0") Integer from,
			@RequestParam(required = false, defaultValue = "10") Integer size) {
		return ResponseEntity.ok(service.getAllUsersRequests(userId, PageRequest.of((from / size), size)));
	}

	@PostMapping
	public ResponseEntity<ItemRequestDto> createRequest(@RequestHeader(HEADER_USER_ID) Long userId,
			@RequestBody ItemRequestDto itemRequestDto) {
		return ResponseEntity.ok(service.createRequest(userId, itemRequestDto));
	}
}
