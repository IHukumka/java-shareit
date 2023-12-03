package ru.practicum.shareit.user.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.user.service.UserTestData.userDto1;
import static ru.practicum.shareit.user.service.UserTestData.userDto2;
import static ru.practicum.shareit.user.service.UserTestData.userDto3;
import static ru.practicum.shareit.user.service.UserTestData.userDtoCreated;
import static ru.practicum.shareit.user.service.UserTestData.userDtoWrongCreated;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService.create(userDto1);
        userService.create(userDto2);
        userService.create(userDto3);
    }

    @Test
    void getTest() {
        UserDto userDtoFromSQL = userService.get(1L);
        assertThat(userDtoFromSQL.getName(), equalTo(userDto1.getName()));
    }

    @Test
    void getUserByWrongIdTest() {
        assertThrows(ResponseStatusException.class, () -> userService.get(100L));
    }

    @Test
    void getAllTest() {
        List<UserDto> users = userService.getAll();
        assertThat(users.size(), equalTo(3));
        userService.create(userDtoCreated);
        assertThat(userService.getAll().size(), equalTo(4));
    }

    @Test
    void createTest() {
        userService.create(userDtoCreated);
        List<UserDto> users = userService.getAll();
        assertThat(users.get(3).getName(), equalTo(userDtoCreated.getName()));
        assertThat(users.get(3).getEmail(), equalTo(userDtoCreated.getEmail()));
    }

    @Test
    void createWithDuplicateEmailTest() {
        assertThrows(DataIntegrityViolationException.class, () -> userService.create(userDtoWrongCreated));
    }

    @Test
    void editTest() {
        userDto2.setName("User2new");
        userDto2.setEmail("User2new@mail.ru");
        UserDto userDtoFromSQL = userService.edit(2L, userDto2);
        assertThat(userDtoFromSQL.getName(), equalTo(userDto2.getName()));
        assertThat(userDtoFromSQL.getEmail(), equalTo(userDto2.getEmail()));
        userDto2.setName("user2");
        userDto2.setEmail("user2@mail.ru");
    }

    @Test
    void editTestWhenNotFound() {
        assertThrows(ResponseStatusException.class, () -> userService.edit(20L, userDto2));
    }

    @Test
    void deleteUserTest() {
        userService.delete(1L);
        assertThat(userService.getAll().size(), equalTo(2));
    }

    @Test
    void deleteUserWrongIdTest() {
        assertThrows(EmptyResultDataAccessException.class, () -> userService.delete(100L));
    }
}