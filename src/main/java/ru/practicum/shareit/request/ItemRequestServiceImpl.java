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
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
	private final ItemRequestMapper itemRequestMapper;
	private final UserService userService;
	private final UserMapper userMapper;
	private final ItemRequestStorage itemRequestRepository;
	private final ItemService itemService;

	@Override
	public List<ItemRequestDto> getAllRequests(long userId) {
		userService.get(userId);
		return itemRequestRepository.findByUser_Id(userId).stream().map(itemRequestMapper::toItemRequestDto)
				.peek(x -> x.setItems(itemService.getRequestItems(x.getId()))).collect(Collectors.toList());
	}

	@Override
	public ItemRequestDto getRequest(long userId, long requestId) {
		userService.get(userId);
		ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(findById(requestId));
		itemRequestDto.setItems(itemService.getRequestItems(requestId));
		return itemRequestDto;
	}

	@Override
	public List<ItemRequestDto> getAllUsersRequests(long userId, Pageable pageable) {
		User user = userMapper.toUser(userService.get(userId));
		return itemRequestRepository.findByUserIsNot(user, pageable).stream().map(itemRequestMapper::toItemRequestDto)
				.peek(x -> x.setItems(itemService.getRequestItems(x.getId()))).collect(Collectors.toList());
	}

	@Override
	public ItemRequestDto createRequest(long userId, ItemRequestDto itemRequestDto) {
		ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
		itemRequest.setCreated(LocalDateTime.now());
		itemRequest.setUser(userMapper.toUser(userService.get(userId)));
		itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
		itemRequestDto.setItems(new ArrayList<>());
		return itemRequestDto;
	}

	@Override
	public ItemRequest findById(long requestId) {
		try {
			return itemRequestRepository.findById(requestId).get();
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}