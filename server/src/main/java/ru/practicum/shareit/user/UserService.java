package ru.practicum.shareit.user;

import java.util.List;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    List<UserDto> getAll();

    UserDto get(long id);

    UserDto edit(long id, UserDto user);

    void clearAll();

    void delete(long id);

    UserDto create(UserDto userDto);

    void checkUser(long id);

}
