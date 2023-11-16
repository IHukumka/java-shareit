package ru.practicum.shareit.item.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.item.service.ItemTestData.commentDto;
import static ru.practicum.shareit.item.service.ItemTestData.itemDto1;
import static ru.practicum.shareit.item.service.ItemTestData.itemDto2;
import static ru.practicum.shareit.item.service.ItemTestData.itemDtoCreated;
import static ru.practicum.shareit.user.service.UserTestData.userDto1;
import static ru.practicum.shareit.user.service.UserTestData.userDto2;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Booking.BookingStatus;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplTest {
	@Autowired
	private final ItemService itemService;
	@Autowired
	private final UserService userService;
	@Autowired
	private final BookingStorage bookingStorage;
	@Autowired
	private final ItemRequestService itemRequestService;
	UserDto bookerDto;
	ItemDto itemDto;

	@BeforeEach
	void setUp() {
		userService.create(userDto1);
		bookerDto = userService.create(userDto2);
		itemDto = itemService.create(1L, itemDto1);
	}

	@Test
	void getTest() {
		Item itemFromSQL = ItemMapper.toItem(itemService.get(1L, 1L));
		assertThat(itemFromSQL.getName(), equalTo(itemDto1.getName()));
	}

	@Test
	void getItemByWrongIdTest() {
		assertThrows(ResponseStatusException.class, () -> itemService.get(100L, 1L));
	}

	@Test
	void getAllTest() {
		List<ItemDto> items = itemService.getAll(1L, PageRequest.of(0, 10));
		assertThat(items.size(), equalTo(1));
	}

	@Test
	void searchTest() {
		List<ItemDto> items = itemService.searchForItems("description1", 0, 10);
		assertThat(items.size(), equalTo(1));
		items = itemService.searchForItems("description2", 0, 10);
		assertThat(items.size(), equalTo(0));
	}

	@Test
	void createTest() {
		itemService.create(1L, itemDtoCreated);
		List<ItemDto> items = itemService.getAll(1L, PageRequest.of(0, 10));
		assertThat(items.get(1).getName().toString(), equalTo(itemDtoCreated.getName().toString()));
	}

	@Test
	void createFailTest() {
		itemDto.setRequestId(100L);
		assertThat(null, equalTo(itemService.create(1L, itemDtoCreated).getRequestId()));
		List<ItemDto> items = itemService.getAll(1L, PageRequest.of(0, 10));
		assertThat(items.get(1).getName().toString(), equalTo(itemDtoCreated.getName().toString()));
	}

	@Test
	void editTest() {
		itemDto1.setName("item1update");
		itemService.edit(1L, itemDto1, 1L);
		List<ItemDto> items = itemService.getAll(1L, PageRequest.of(0, 10));
		assertThat(items.get(0).getName(), equalTo("item1update"));
	}

	@Test
	void editWrongUserIdTest() {
		assertThrows(ResponseStatusException.class, () -> itemService.edit(1L, itemDto1, 2L));
	}

	@Test
	void deleteTest() {
		itemService.delete(itemDto.getId());
		assertThrows(ResponseStatusException.class, () -> itemService.get(itemDto.getId(), 1L));
	}

	@Test
	void createCommentTest() {
		Booking booking = Booking.builder().start(LocalDateTime.of(2022, 8, 1, 12, 15, 1))
				.end(LocalDateTime.of(2022, 8, 2, 12, 15, 1)).item(ItemMapper.toItem(itemDto))
				.booker(UserMapper.toUser(bookerDto)).status(BookingStatus.APPROVED).build();
		bookingStorage.save(booking);
		itemService.createComment(1L, 2L, commentDto, Pageable.ofSize(10));
		List<ItemDto> items = itemService.getAll(1L, PageRequest.of(0, 10));
		assertThat(items.get(0).getComments().get(0).getText(), equalTo(commentDto.getText()));
	}

	@Test
	void createCommentFailTest() {
		assertThrows(IllegalArgumentException.class,
				() -> itemService.createComment(1L, 1L, commentDto, Pageable.ofSize(10)));
	}

	@Test
	void getRequestItemsTest() {
		ItemRequestDto itemRequest1 = ItemRequestDto.builder().description("itemRequest1").build();
		itemRequestService.createRequest(2L, itemRequest1);
		itemService.create(1L, itemDto2);
		List<ItemDto> items = itemService.getRequestItems(1L);
		assertThat(items.size(), equalTo(1));
	}

	@Test
	void getItemWithBookingsById() {
		ItemDto item = itemService.get(1L, 1L);
		assertThat(item.getNextBooking(), equalTo(null));
		assertThat(item.getLastBooking(), equalTo(null));
		assertThat(item.getName(), equalTo(itemDto1.getName()));
	}
}