package ru.practicum.shareit.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestStorage;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

	private final ItemStorage storage;
	private final ItemMapper mapper;
	private final UserMapper userMapper;
	private final UserService userService;
	private final ItemRequestStorage itemRequestStorage;

	@Override
	public List<ItemDto> getAll(Long userId) {
		if (userId != null) {
			return storage.findByUser_Id(userId).stream().map(this.mapper::toItemDto).collect(Collectors.toList());
		} else {
			return storage.findAll().stream().map(this.mapper::toItemDto).collect(Collectors.toList());
		}
	}

	@Override
	public List<ItemDto> getRequestItems(long requestId) {
		return storage.findByRequest_Id(requestId).stream().map(mapper::toItemDto).peek(x -> x.setRequestId(requestId))
				.collect(Collectors.toList());
	}

	@Override
	public List<ItemDto> searchForItems(String text, Integer from, Integer size) {
		List<ItemDto> result = new ArrayList<>();
		if (text.isBlank()) {
			return result;
		}
		if (size < 1 || from < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		String query = text.toLowerCase();
		return storage.findAllItemsByDescriptionContainingIgnoreCaseAndAvailableTrue(query).stream()
				.map(this.mapper::toItemDto).collect(Collectors.toList());
	}

	@Override
	public ItemDto create(Long userId, ItemDto itemDto) {
		ItemRequest itemRequest = null;
		Item item = mapper.toItem(itemDto);
		if (itemDto.getRequestId() != null) {
			itemRequest = itemRequestStorage.findById(itemDto.getRequestId()).get();
		}
		userService.checkUser(userId);
		try {
			this.checkItem(item.getId());
		} catch (RuntimeException e) {
		}
		;
		if (item.getAvailable() == null || item.getAvailable() == false) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		item.setUser(userMapper.toUser(userService.get(userId)));
		item.setRequest(itemRequest);
		ItemDto newItemDto = mapper.toItemDto(storage.save(item));
		newItemDto.setRequestId(itemDto.getRequestId());
		return newItemDto;
	}

	@Override
	public ItemDto edit(Long id, ItemDto itemDto, Long userId) {
		Item newItem = this.mapper.toItem(itemDto);
		this.checkItem(id);
		this.userService.checkUser(userId);
		Item oldItem = this.storage.findById(id).get();
		if (oldItem.getUser().getId() != userId) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		Optional.ofNullable(newItem.getName()).ifPresent(oldItem::setName);
		Optional.ofNullable(newItem.getDescription()).ifPresent(oldItem::setDescription);
		Optional.ofNullable(newItem.getAvailable()).ifPresent(oldItem::setAvailable);
		oldItem.setUser(this.userMapper.toUser(this.userService.get(userId)));
		return this.mapper.toItemDto(this.storage.save(oldItem));
	}

	@Override
	public void clearAll() {
		this.storage.deleteAll();

	}

	@Override
	public void delete(Long id) {
		this.storage.deleteById(id);
	}

	@Override
	public ItemDto get(Long id) {
		this.checkItem(id);
		return this.mapper.toItemDto(storage.findById(id).get());
	}

	private void checkItem(Long id) {
		boolean isPresent = this.storage.existsById(id);
		if (!isPresent) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
