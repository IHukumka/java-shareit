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
    public ResponseEntity<Object> getAllRequests(@RequestHeader(HEADER_USER_ID) Long userId) {
        return client.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(HEADER_USER_ID) Long userId, @PathVariable Long requestId) {
        return client.getRequest(userId.longValue(), requestId.longValue());
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsersRequests(@RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (from.longValue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return client.getAllUsersRequests(userId.longValue(), from.intValue(), size.intValue());
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(HEADER_USER_ID) Long userId,
            @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return client.createRequest(userId.longValue(), itemRequestDto);
    }
}
