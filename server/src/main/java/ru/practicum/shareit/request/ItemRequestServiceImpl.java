package ru.practicum.shareit.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

@Slf4j
@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserService userService;
    private final ItemRequestStorage itemRequestStorage;
    private final ItemService itemService;

    @Override
    public ItemRequestDto createRequest(long userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setUser(UserMapper.toUser(userService.get(userId)));
        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequestStorage.save(itemRequest));
        itemRequestDto.setItems(new ArrayList<>());
        return itemRequestDto;
    }

    @Override
    public ItemRequest findById(long requestId) {
        try {
            return itemRequestStorage.findById(requestId).get();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<ItemRequestDto> getAllRequests(long userId) {
        userService.get(userId);
        return itemRequestStorage.findByUser_Id(userId).stream().map(ItemRequestMapper::toItemRequestDto)
                .peek(x -> x.setItems(itemService.getRequestItems(x.getId()))).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllUsersRequests(long userId, Pageable pageable) {
        User user = UserMapper.toUser(userService.get(userId));
        return itemRequestStorage.findByUserIsNot(user, pageable).stream().map(ItemRequestMapper::toItemRequestDto)
                .peek(x -> x.setItems(itemService.getRequestItems(x.getId()))).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequest(long userId, long requestId) {
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(findById(requestId));
        itemRequestDto.setRequestor(userService.get(userId));
        try {
            itemRequestDto.setItems(itemService.getRequestItems(requestId));
        } catch (ResponseStatusException e) {
            log.debug(e.getReason());
        }
        return itemRequestDto;
    }
}