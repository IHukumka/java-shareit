package ru.practicum.shareit.item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Booking.BookingStatus;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

	private final ItemStorage storage;
	private final UserService userService;
	private final ItemRequestStorage itemRequestStorage;
	private final BookingStorage bookingStorage;
	private final CommentStorage commentStorage;

	private void checkItem(Long id) {
		boolean isPresent = this.storage.existsById(id);
		if (!isPresent) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ItemDto create(Long userId, ItemDto itemDto) {
		userService.checkUser(userId);
		itemDto.setUser(userService.get(userId));
		Item item = ItemMapper.toItem(itemDto);
		if (itemDto.getAvailable() == false) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		ItemRequest itemRequest = null;
		if (itemDto.getRequestId() != null) {
			try {
				itemRequest = itemRequestStorage.findById(itemDto.getRequestId()).get();
			} catch (NoSuchElementException e) {
				log.debug(e.getMessage());
			}
		}
		item.setUser(UserMapper.toUser(userService.get(userId)));
		item.setRequest(itemRequest);
		ItemDto newItemDto = ItemMapper.toItemDto(storage.save(item));
		newItemDto.setRequestId(itemDto.getRequestId());
		return newItemDto;
	}

	@Override
	public CommentDto createComment(long itemId, long userId, CommentDto commentDto, Pageable pageable) {
		Item item = ItemMapper.toItem(get(itemId, userId));
		User user = UserMapper.toUser(userService.get(userId));
		if (!bookingStorage.findByItem_IdAndBooker_IdAndStatusAndEndBefore(itemId, userId, BookingStatus.APPROVED,
				LocalDateTime.now(), pageable).isEmpty()) {
			Comment comment = CommentMapper.toComment(commentDto);
			comment.setAuthor(user);
			comment.setItem(item);
			comment.setCreated(LocalDateTime.now().plusHours(3L));
			CommentDto newCommentDto = CommentMapper.toDto(commentStorage.save(comment));
			newCommentDto.setAuthorName(user.getName());
			return newCommentDto;
		} else {
			throw new IllegalArgumentException("Ошибка входящих данных");
		}
	}

	@Override
	public void delete(Long id) {
		this.storage.deleteById(id);
	}

	@Override
	public ItemDto edit(Long id, ItemDto itemDto, Long userId) {
		this.checkItem(id);
		this.userService.checkUser(userId);
		itemDto.setUser(userService.get(userId));
		Item newItem = ItemMapper.toItem(itemDto);
		Item oldItem = this.storage.findById(id).get();
		if (!oldItem.getUser().getId().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		Optional.ofNullable(newItem.getName()).ifPresent(oldItem::setName);
		Optional.ofNullable(newItem.getDescription()).ifPresent(oldItem::setDescription);
		Optional.ofNullable(newItem.getAvailable()).ifPresent(oldItem::setAvailable);
		oldItem.setUser(UserMapper.toUser(this.userService.get(userId)));
		return ItemMapper.toItemDto(this.storage.save(oldItem));
	}

	private List<CommentDto> findComments(ItemDto itemDto) {
		return commentStorage.findAll().stream().filter(x -> x.getItem().getId().equals(itemDto.getId()))
				.map(c -> new CommentDto(c.getId(), c.getText(), c.getAuthor().getName(), c.getCreated()))
				.collect(Collectors.toList());
	}

	private ItemDto findLastBooking(ItemDto itemDto, long userId, List<Booking> lastBookings) {
		try {
			itemDto.setLastBooking(BookingMapper.toBookingDtoL(lastBookings.stream()
					.filter(x -> x.getItem().getId().equals(itemDto.getId())).collect(Collectors.toList()).get(0)));
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return itemDto;
	}

	private ItemDto findNextBooking(ItemDto itemDto, long userId, List<Booking> nextBookings) {
		try {
			itemDto.setNextBooking(BookingMapper.toBookingDtoL(nextBookings.stream()
					.filter(x -> x.getItem().getId().equals(itemDto.getId())).collect(Collectors.toList()).get(0)));
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return itemDto;
	}

	@Override
	public ItemDto get(Long id, Long userId) {
		this.checkItem(id);
		userService.checkUser(userId);
		Item item = storage.findById(id).get();
		ItemDto result = ItemMapper.toItemDto(item);
		Pageable pageable = PageRequest.of(0, 10);
		if (userId.equals(result.getUser().getId())) {
			try {
				result.setLastBooking(
						BookingMapper.toBookingDtoL(bookingStorage
								.findByItem_IdAndStatusAndStartBeforeOrderByStartDesc(result.getId(),
										BookingStatus.APPROVED, LocalDateTime.now(), pageable)
								.stream().collect(Collectors.toList()).get(0)));
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			try {
				result.setNextBooking(
						BookingMapper.toBookingDtoL(bookingStorage
								.findByItem_IdAndStatusAndStartAfterOrderByStartAsc(result.getId(),
										BookingStatus.APPROVED, LocalDateTime.now(), pageable)
								.stream().collect(Collectors.toList()).get(0)));
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		result.setComments(findComments(result));
		return result;
	}

	@Override
	public List<ItemDto> getAll(Long userId, Pageable pageable) {
		List<ItemDto> result = storage.findByUser_IdOrderById(userId, pageable).stream().map(ItemMapper::toItemDto)
				.collect(Collectors.toList());
		List<Booking> nextBookings = bookingStorage
				.findAllByStatusAndStartAfterOrderByStartAsc(BookingStatus.APPROVED, LocalDateTime.now(), pageable)
				.stream().collect(Collectors.toList());
		List<Booking> lastBookings = bookingStorage
				.findAllByStatusAndStartBeforeOrderByStartDesc(BookingStatus.APPROVED, LocalDateTime.now(), pageable)
				.stream().collect(Collectors.toList());
		for (ItemDto itemDto : result) {
			itemDto = findNextBooking(itemDto, userId, nextBookings);
			itemDto = findLastBooking(itemDto, userId, lastBookings);
			itemDto.setComments(findComments(itemDto));
		}
		return result;
	}

	@Override
	public List<ItemDto> getRequestItems(long requestId) {
		return storage.findByRequest_Id(requestId).stream().map(ItemMapper::toItemDto)
				.peek(x -> x.setRequestId(requestId)).collect(Collectors.toList());
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
				.map(ItemMapper::toItemDto).collect(Collectors.toList());
	}
}