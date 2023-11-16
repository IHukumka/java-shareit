package ru.practicum.shareit.request.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.user.service.UserTestData.userDto1;
import static ru.practicum.shareit.user.service.UserTestData.userDto2;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestServiceImplTest {
	@Autowired
	private final ItemRequestService itemRequestService;
	@Autowired
	private UserService userService;

	private ItemRequestDto itemRequest1;
	private ItemRequestDto itemRequest2;
	long userId;

	@BeforeEach
	void setUp() {
		itemRequest1 = ItemRequestDto.builder().description("itemRequest1").build();
		itemRequest2 = ItemRequestDto.builder().description("itemRequest2").build();
		userId = userService.create(userDto1).getId();
		itemRequestService.createRequest(1L, itemRequest1);
	}

	@Test
	void getAllRequestsTest() {
		List<ItemRequestDto> itemRequestDtoFromSQL = itemRequestService.getAllRequests(userId);
		assertThat(itemRequestDtoFromSQL.size(), equalTo(1));
	}

	@Test
	void getAllRequestsWrongIdTest() {
		assertThrows(ResponseStatusException.class, () -> itemRequestService.getAllRequests(10L));
	}

	@Test
	void getRequestTest() {
		long id = itemRequestService.createRequest(userId, itemRequest2).getId();
		assertThat(itemRequestService.getRequest(userId, id).getDescription(), equalTo(itemRequest2.getDescription()));
	}

	@Test
	void getRequestWrongIdTest() {
		assertThrows(ResponseStatusException.class, () -> itemRequestService.getRequest(userId, 2L));
	}

	@Test
	void getRequestWrongUserIdTest() {
		long id = itemRequestService.createRequest(userId, itemRequest2).getId();
		assertThrows(ResponseStatusException.class, () -> itemRequestService.getRequest(100L, id));
	}

	@Test
	void getAllUsersRequests() {
		long user2Id = userService.create(userDto2).getId();
		itemRequestService.createRequest(user2Id, itemRequest2);
		List<ItemRequestDto> itemRequestDtoFromSQL = itemRequestService.getAllUsersRequests(user2Id,
				PageRequest.of(0, 10));
		assertThat(itemRequestDtoFromSQL.size(), equalTo(1));
	}

	@Test
	void createRequest() {
		itemRequestService.createRequest(userId, itemRequest2);
		List<ItemRequestDto> itemRequestDtoFromSQL = itemRequestService.getAllRequests(userId);
		assertThat(itemRequestDtoFromSQL.get(1).getDescription(), equalTo(itemRequest2.getDescription()));
		assertThat(itemRequestDtoFromSQL.get(1).getItems().size(), equalTo(0));
	}
}